FROM openjdk:17-bullseye

WORKDIR /app

# Gerekli dizinleri oluşturur
RUN mkdir -p /app/lib

# PostgreSQL JDBC Driver'ı indirir
RUN apt-get update && apt-get install -y curl
RUN curl -o /app/lib/postgresql-42.6.0.jar https://jdbc.postgresql.org/download/postgresql-42.6.0.jar

# Java kaynak kodlarını ve derleme scriptini kopyalar
COPY src /app/src
COPY compile.sh /app/compile.sh

# Derleme scriptine çalıştırma izni ver ve çalıştırır
RUN chmod +x /app/compile.sh && /app/compile.sh

# Backend'i çalıştırırken JDBC Driver'ı classpath'e ekler
CMD ["java", "-cp", "out:lib/postgresql-42.6.0.jar", "Main"]
