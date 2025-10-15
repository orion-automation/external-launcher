create table if not exists ENHACEMENT_FORM
(
    ID varchar(64) not null primary key,
    NAME_TXT varchar(64) not null,
    TENANT_TXT varchar(64) default null,
    TYPE_TXT varchar(16) default '',
    DEFINITION_KEY_TXT VARCHAR(255) default '',
    FROM_DATA text default '',
    CREATED_TS bigint,
    UPDATED_TS bigint,
    CREATE_BY_TXT varchar(20) default null,
    UPDATED_BY_TXT varchar(20) default null,
    DELETE_FG smallint default 0
);

MERGE INTO ENHACEMENT_FORM
    (id, name_txt, tenant_txt, type_txt, DEFINITION_KEY_TXT, from_data, created_ts, updated_ts, create_by_txt, updated_by_txt, delete_fg)
    VALUES ('324234234234', 'form1', NULL, 'from', '324234234234', '[{"key": "value"}]', NULL, NULL, NULL, NULL, 0);