# OpenJDK 17 imajını kullanır
FROM openjdk:17

# Çalışma dizinini belirler
WORKDIR /app

# Kaynak kodları kopyalar
COPY src/Main.java .

# Java dosyasını derler
RUN javac Main.java

# Programı çalıştırır
CMD ["java", "Main"]
