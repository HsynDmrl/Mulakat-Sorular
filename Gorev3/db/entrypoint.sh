#!/bin/bash
set -e

# Start PostgreSQL server in the background using the official entrypoint
/usr/local/bin/docker-entrypoint.sh postgres &

# Wait for PostgreSQL to start
until pg_isready -h localhost -p 5432 -U "$POSTGRES_USER"; do
  echo "Waiting for PostgreSQL to start..."
  sleep 2
done

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "postgres" <<-EOSQL
    DROP DATABASE IF EXISTS $POSTGRES_DB;
    CREATE DATABASE $POSTGRES_DB;
EOSQL


# Keep the container running
wait
