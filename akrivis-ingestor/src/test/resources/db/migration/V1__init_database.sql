create table job
(
    id         serial primary key,
    type       varchar(255) not null,
    endpoint   varchar(255) not null,
    cron       varchar(255) not null,
    status     varchar(255) not null,
    created_at timestamp    not null default now(),
    last_run   timestamp null
);

-- TODO This should be removed as we don't want leaking of mock impl in production
insert into job (id, type, endpoint, cron, status)
values (1, 'GitHubMock', '/issues', '0/5 * * * * ?', 'SCHEDULED');

SELECT setval('job_id_seq', (SELECT MAX(id) FROM job));

create table raw_data
(
    id           serial primary key,
    data         jsonb     not null,
    created_at   timestamp not null default now(),
    fk_job serial    not null,
    foreign key (fk_job) references job (id)
);