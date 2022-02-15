create table executor
(
    id                 bigserial    NOT NULL,
    first_name         varchar(30)  NOT NULL,
    last_name          varchar(30)  NOT NULL,
    created_date       timestamp    NOT NULL default NOW(),
    created_by         varchar(200) NOT NULL default 'system',
    last_modified_date timestamp    NOT NULL default NOW(),
    last_modified_by   varchar(200) NOT NULL default 'system',

    CONSTRAINT pk__executor_id PRIMARY KEY (id)
);

create table task
(
    id                 bigserial    NOT NULL,
    name               varchar(30)  NOT NULL,
    description        varchar(200) NOT NULL,
    group_name         varchar(100) NOT NULL,
    created_date       timestamp    NOT NULL default NOW(),
    created_by         varchar(200) NOT NULL default 'system',
    last_modified_date timestamp    NOT NULL default NOW(),
    last_modified_by   varchar(200) NOT NULL default 'system',

    CONSTRAINT pk__task_id PRIMARY KEY (id)
);

create table sub_task
(
    id                 bigserial    NOT NULL,
    name               varchar(30)  NOT NULL,
    description        varchar(200) NOT NULL,
    created_date       timestamp    NOT NULL default NOW(),
    created_by         varchar(200) NOT NULL default 'system',
    last_modified_date timestamp    NOT NULL default NOW(),
    last_modified_by   varchar(200) NOT NULL default 'system',

    CONSTRAINT pk__sub_task_id PRIMARY KEY (id)
);

create table executor_task
(
    id          bigserial NOT NULL,
    executor_id bigint    NOT NULL,
    task_id     bigint    NOT NULL,

    CONSTRAINT pk__executor_task_id PRIMARY KEY (id),
    CONSTRAINT unq__executor_task UNIQUE (executor_id, task_id),
    CONSTRAINT fk__executor_task_to_executor FOREIGN KEY (executor_id)
        REFERENCES executor (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk__executor_task_to_task FOREIGN KEY (task_id)
        REFERENCES task (id) ON DELETE CASCADE ON UPDATE CASCADE
);

create table task_sub_task
(
    id          bigserial NOT NULL,
    task_id     bigint    NOT NULL,
    sub_task_id bigint    NOT NULL,

    CONSTRAINT pk__task_sub_task_id PRIMARY KEY (id),
    CONSTRAINT unq__task_sub_task UNIQUE (sub_task_id, task_id),
    CONSTRAINT fk__task_sub_task_to_task FOREIGN KEY (task_id)
        REFERENCES task (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk__task_sub_task_to_sub_task FOREIGN KEY (sub_task_id)
        REFERENCES sub_task (id) ON DELETE CASCADE ON UPDATE CASCADE
);
