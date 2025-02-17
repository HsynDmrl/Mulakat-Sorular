# ==========================
# GENEL DOCKER KOMUTLARI
# ==========================

up: ## Tüm servisleri başlatır (Docker ile)
	docker-compose up -d --build

down: ## Tüm servisleri durdur ve kaldırır
	docker-compose down -v

logs: ## Logları gösterir
	docker-compose logs -f

ps: ## Çalışan container'ları gösterir
	docker-compose ps

re: ## Servisleri yeniden başlatır
	make down
	make up
	
# ==========================
# BACKEND DERLEME & ÇALIŞTIRMA
# ==========================

JAVAC=javac
JAVA=java
SRC=$(shell find backend/src -name "*.java")
OUT=backend/out
LIB=backend/lib/postgresql-42.6.0.jar
PORT=8080

DB_URL=jdbc:postgresql://localhost:5432/mydatabase
DB_USER=myuser
DB_PASSWORD=mypassword

backend-compile: ## Backend'i derle (Core Java + JDBC + JSON)
	mkdir -p $(OUT)
	$(JAVAC) -d $(OUT) -cp "$(LIB):backend/lib/json-20210307.jar" $(SRC)

backend-kill-port: ## 8080 portunu kullanan süreci otomatik bulur ve kapatır
	@if lsof -i :$(PORT) > /dev/null 2>&1; then \
		echo "8080 portunu kullanan süreç bulundu, kapatılıyor..."; \
		kill -9 $$(lsof -ti :$(PORT)); \
	else \
		echo "8080 portu boşta."; \
	fi

backend-stop-docker: ## Docker içindeki backend'i durdurur
	@if docker ps --format '{{.Names}}' | grep -q "java_backend"; then \
		echo "Docker içindeki backend çalışıyor, kapatılıyor..."; \
		docker stop java_backend && docker rm java_backend; \
	else \
		echo "Docker backend zaten kapalı."; \
	fi

backend-run: backend-stop-docker backend-kill-port backend-compile ## Backend'i temizleyip çalıştır
	@sleep 2  # Portun tam kapanmasını beklemek için 2 saniye bekleyelim
	@echo "8080 portu kapatıldı, backend başlatılıyor..."
	DB_URL=$(DB_URL) DB_USER=$(DB_USER) DB_PASSWORD=$(DB_PASSWORD) $(JAVA) -cp "$(OUT):$(LIB)" Main

backend-clean:
	rm -rf $(OUT)

# ==========================
# FRONTEND KOMUTLARI
# ==========================

frontend-build: 
	docker-compose up -d --build frontend

frontend-stop:
	docker-compose stop frontend

frontend-start: frontend-stop frontend-build
	@echo "Frontend başlatıldı: http://localhost:3000"

# ==========================
# START KOMUTU (BACKEND + DOCKER + FRONTEND)
# ==========================
start: ## Tüm servisleri başlatır (Backend + Docker + Frontend)
	make up
	@$(call wait_for_service,PostgreSQL,5432)
	make backend-run
	@$(call wait_for_service,Backend,8080)
	make frontend-start

# ==========================
# YARDIMCI FONKSİYON
# ==========================

define wait_for_service
	@echo "$(1) servisi için bekleniyor..."
	@while ! nc -z localhost $(2); do sleep 1; done
	@echo "$(1) servisi başlatıldı. port: $(2)"
endef

# ==========================
# TEMİZLEME KOMUTU (BACKEND + DOCKER)
# ==========================

clean: ## Tüm sistem temizliği (backend derleme + docker durdurma)
	make backend-clean
	make down

# ==========================
# YARDIM KOMUTU
# ==========================

help: ## Kullanılabilir komutları gösterir
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'
