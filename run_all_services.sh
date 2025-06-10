#!/bin/bash

# Kolory dla czytelności
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Sprawdzenie argumentów
if [ $# -eq 0 ]; then
    echo -e "${RED}❌ Błąd: Musisz podać login do Docker Hub jako argument${NC}"
    echo -e "${YELLOW}Użycie: $0 <dockerhub_login>${NC}"
    echo -e "${YELLOW}Przykład: $0 moj_login${NC}"
    exit 1
fi

DOCKERHUB_USER="$1"
SERVICES=("notification-service" "payment-service" "reservation-service" "users-service")
K8S_MANIFEST_DIR="./k8s"

echo -e "${BLUE}🚀 Budowanie i wysyłanie obrazów Docker dla użytkownika: $DOCKERHUB_USER${NC}\n"

# Funkcja do budowania i wysyłania obrazu
build_and_push_service() {
    local service=$1
    local image_name="$DOCKERHUB_USER/$service:latest"

    echo -e "${YELLOW}📦 Przetwarzam serwis: $service${NC}"

    if [ -d "$service" ]; then
        echo -e "${BLUE}   - Budowanie obrazu Docker: $image_name${NC}"
        docker build -t "$image_name" "./$service"

        if [ $? -eq 0 ]; then
            echo -e "${GREEN}   ✅ Obraz zbudowany pomyślnie${NC}"

            echo -e "${BLUE}   - Wysyłanie obrazu na Docker Hub...${NC}"
            docker push "$image_name"

            if [ $? -eq 0 ]; then
                echo -e "${GREEN}   ✅ Obraz wysłany pomyślnie: $image_name${NC}\n"
            else
                echo -e "${RED}   ❌ Błąd podczas wysyłania obrazu: $image_name${NC}\n"
                return 1
            fi
        else
            echo -e "${RED}   ❌ Błąd podczas budowania obrazu dla: $service${NC}\n"
            return 1
        fi
    else
        echo -e "${RED}   ❌ Katalog $service nie istnieje${NC}\n"
        return 1
    fi
}

# Logowanie do Docker Hub
echo -e "${YELLOW}🔐 Logowanie do Docker Hub...${NC}"
echo -e "${BLUE}Zaloguj się na konto: $DOCKERHUB_USER${NC}"
docker login

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Błąd logowania do Docker Hub${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Pomyślnie zalogowano do Docker Hub${NC}\n"

# Buduj i wysyłaj wszystkie serwisy
for SERVICE in "${SERVICES[@]}"; do
    build_and_push_service "$SERVICE"
done

# Wdrażanie do Kubernetesa
if [ -d "$K8S_MANIFEST_DIR" ]; then
    echo -e "${YELLOW}☸️  Wdrażam konfiguracje Kubernetes z folderu: $K8S_MANIFEST_DIR${NC}"
    kubectl apply -f "$K8S_MANIFEST_DIR"

    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Kubernetes zaktualizowany pomyślnie!${NC}"
    else
        echo -e "${RED}❌ Błąd podczas wdrażania do Kubernetes${NC}"
        exit 1
    fi
else
    echo -e "${YELLOW}⚠️  Katalog $K8S_MANIFEST_DIR nie istnieje - pomijam wdrożenie Kubernetes${NC}"
fi

echo -e "\n${GREEN}🎉 Wszystkie obrazy wypchnięte i Kubernetes zaktualizowany!${NC}"
echo -e "${YELLOW}💡 Sprawdź status wdrożenia: kubectl get pods${NC}"
echo -e "${YELLOW}💡 Sprawdź serwisy: kubectl get services${NC}"