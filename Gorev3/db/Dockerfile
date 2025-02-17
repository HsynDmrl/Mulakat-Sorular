# PostgreSQL 15 tabanlı bir özel image oluşturuyoruz
FROM postgres:15

# pgcrypto uzantısını etkinleştirmek için postgresql-contrib paketini yüklüyoruz
RUN apt-get update && apt-get install -y postgresql-contrib

# init.sql dosyasını otomatik olarak çalıştırılacak klasöre kopyala
COPY init.sql /docker-entrypoint-initdb.d/

# Container başlatıldığında PostgreSQL'in çalışmasını sağla
CMD ["postgres"]
