# DDL Scripts

Canonical DDL scripts for the `myroom` izakaya reservation schema.

## Files

| File | Purpose |
|------|---------|
| `V1__create_tables.sql` | `room`, `menu`, `reservation` tables + indexes + check constraints |
| `V2__seed_data.sql`     | Sample rooms and izakaya menu (drinks / food) |

## Compatibility

Written for **H2 (MySQL mode)** and **MySQL 8.x**.
- Uses `AUTO_INCREMENT`, `BOOLEAN`, `TIMESTAMP`.
- `CHECK` constraints enforce enum values and non-negative integers.

## Runtime bootstrap

Spring Boot loads copies placed at:
- `src/main/resources/db/schema.sql`
- `src/main/resources/db/data.sql`

Keep those in sync with the files in this folder. When migrating to
Flyway/Liquibase, point the tool at this directory and drop the duplicates.

## ERD

See [../../docs/erd.md](../../docs/erd.md).
