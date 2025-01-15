package org.kubesmarts.akrivis.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.kubesmarts.akrivis.dbmodel.Job;
import org.kubesmarts.akrivis.dbmodel.JobRepository;
import org.kubesmarts.akrivis.dbmodel.JobStatus;
import org.kubesmarts.akrivis.dbmodel.RawData;
import org.kubesmarts.akrivis.utility.BackstageResponse;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Path("/job")
public class JobResource {

    private static final Logger LOG = Logger.getLogger(JobResource.class.getName());


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

    public record JobRequest(String endpoint, String type, String cron) {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public JobResponse create(JobRequest jobRequest) throws JsonProcessingException {

        Job job = new Job();

        job.endpoint = jobRequest.endpoint;
        job.type = jobRequest.type;
        job.cron = jobRequest.cron;
        job.status = JobStatus.DRAFT;

        jobRepository.persist(job);

        return new JobResponse(job);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/test")
    public BackstageResponse<RawDataDetailDTO> test(@PathParam("id") Long jobId) throws JsonProcessingException {

        final Job job = jobRepository.findById(jobId);
        final IngestorHttpClient client = IngestorHttpClient.findHttpClient(job.type);
        final String data = client.fetchData(job.endpoint);
        final RawData rawData = new RawData();
        rawData.data = data;
        rawData.job = job;
        rawData.createdAt = Instant.now();


        return new BackstageResponse<>(new RawDataDetailDTO(rawData));
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
        jobRepository.delete(jobId);
        LOG.info("Job Deleted: " + jobId);
        return Response.noContent().build();
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public BackstageResponse<List<JobResponse>> all() throws JsonProcessingException {
        return new BackstageResponse<>(
                jobRepository.findAll(Sort.ascending("id"))
                        .stream()
                        .map(JobResponse::new)
                        .toList());
    }

    public record RawDataDTO(Long id, Instant createdAt) {
        public RawDataDTO(RawData rawData) {
            this(rawData.id, rawData.createdAt);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/data")
    public BackstageResponse<List<RawDataDTO>> rawData(@PathParam("id") Long jobId) throws JsonProcessingException {
        return new BackstageResponse<>(jobRepository.findRawDataByJobId(jobId)
                .stream()
                .map(RawDataDTO::new).toList());
    }

    public record RawDataDetailDTO(Long id, Instant createdAt, JsonNode data) {
        public RawDataDetailDTO(RawData rawData) {
            this(rawData.id, rawData.createdAt, createJsonObject(rawData.data));
        }

        private static JsonNode createJsonObject(String data) {
            ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            try {
                return objectMapper.readValue(data, JsonNode.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/data/{dataId}")
    public Response rawData(@PathParam("id") Long jobId, @PathParam("dataId") Long dataId) throws JsonProcessingException {
        return jobRepository.findRawDataById(jobId, dataId)
                .map(RawDataDetailDTO::new)
                .map(BackstageResponse::new)
                .map(Response::ok)
                .orElse(Response.status(Status.NO_CONTENT)).build();
    }
}
