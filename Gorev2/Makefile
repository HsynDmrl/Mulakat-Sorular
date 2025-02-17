# Gorev Adı
IMAGE_NAME=gorev2-image
CONTAINER_NAME=gorev2-container

# Docker Image'ı oluşturur
build:
	docker build -t $(IMAGE_NAME) .

# Docker Container'ı çalıştırır
run:
	docker run --rm --name $(CONTAINER_NAME) $(IMAGE_NAME)

# Docker Container'ı arka planda çalıştırır
up:
	docker run -d --name $(CONTAINER_NAME) $(IMAGE_NAME)

# Çalışan Container'ı durdur
stop:
	docker stop $(CONTAINER_NAME)

# Temizler
clean:
	docker rmi -f $(IMAGE_NAME)
