package org.kie.akrivis.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.quarkus.panache.common.Sort;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import io.quarkus.qute.CheckedTemplate;
import org.akrivis.dbmodel.Job;
import org.akrivis.dbmodel.JobRepository;
import org.akrivis.dbmodel.JobStatus;
import org.akrivis.dbmodel.RawData;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/jobh")
public class JobHtmxResource {

    private static final Logger LOG = Logger.getLogger(JobHtmxResource.class.getName());


    @Inject
    JobRepository jobRepository;

    @Inject
    JobExecutor jobExecutor;

    @Inject
    JobScheduler jobScheduler;

    public record JobResponse(Long id, String endpoint, String type, String cron, JobStatus status, Instant lastRun) {
        public JobResponse(Job job) {
            this(job.id, job.endpoint, job.type, job.cron, job.status, job.lastRun);
        }

    }

    public static final class JobRequest {
        @FormParam("endpoint")
        public String endpoint;
        @FormParam("type")
        public String type;
        @FormParam("cron")
        public String cron;
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public Response create(JobRequest jobRequest) {

        Job job = new Job();

        job.endpoint = jobRequest.endpoint;
        job.type = jobRequest.type;
        job.cron = jobRequest.cron;
        job.status = JobStatus.DRAFT;

        jobRepository.persist(job);

        return Response.ok(allJobs()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/test")
        public Response test(@PathParam("id") Long jobId) throws JsonProcessingException {

        Job job = jobRepository.findById(jobId);
        RawData run = jobExecutor.run(job.id, IngestorHttpClient.findHttpClient(job.type));

        return Response.ok(findRawData(jobId)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/activate")
    @Transactional
    public Response activate(@PathParam("id") Long jobId) throws JsonProcessingException {

        Job job = jobRepository.findById(jobId);
        job.status = JobStatus.SCHEDULED;
        jobRepository.persist(job);

        jobScheduler.addJob(job);

        return Response.ok(jobId).build();
    }
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long jobId) throws JsonProcessingException {
        jobRepository.deleteJobRawData(jobId);
        LOG.info("Job Deleted: " + jobId);
        return Response.ok(allJobs()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response find(@PathParam("id") Long jobId) throws JsonProcessingException {

        Optional<Job> job = jobRepository.findByIdOptional(jobId);
        if (job.isEmpty()) {
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.ok(new JobResponse(job.get())).build();
    }

    @CheckedTemplate
    public static final class Templates {
        public static native TemplateInstance jobsTemplate(List<Job> jobs);

        public static native TemplateInstance rawDataTemplate(List<RawData> rawData, Long jobId);

        public static native TemplateInstance rawDataDetail(RawData rawData, String prettyPrintedData);

    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String all() {
        return allJobs();
    }

    private String allJobs() {
        List<Job> jobs = jobRepository.findAll(Sort.ascending("id")).list();
        return Templates.jobsTemplate(jobs).render();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("{id}/data")
    public String rawData(@PathParam("id") Long jobId) throws JsonProcessingException {
        return findRawData(jobId);
    }

    private String findRawData(Long jobId) {
        List<RawData> list = jobRepository.findRawDataByJobId(jobId);
        return Templates.rawDataTemplate(list, jobId).render();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/data/{dataId}")
    public Response rawData(@PathParam("id") Long jobId, @PathParam("dataId") Long dataId)
            throws JsonProcessingException {
        Optional<RawData> optRawDataById = jobRepository.findRawDataById(jobId, dataId);
        if(optRawDataById.isEmpty()) {
            return Response.status(Status.NOT_FOUND).build();
        }

        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        RawData rawData = optRawDataById.get();
        String uglyValue = rawData.data;
        JsonNode jsonObject = objectMapper.readValue(uglyValue, JsonNode.class);
        LOG.info("JSON Object: " + jsonObject);
        String prettyJson = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(jsonObject);
        LOG.info("Pretty json: " + prettyJson);

        String responseBody = Templates.rawDataDetail(rawData, prettyJson).render();

        return Response.ok(responseBody).build();
    }
}
