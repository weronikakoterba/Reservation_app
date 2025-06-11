# Reservation App

**Autorzy:** [`Weronika Koterba`](https://github.com/weronikakoterba), [`Julita Zamroczyńska`](https://github.com/zamrokjulita)

Aplikacja do zarządzania rezerwacjami z architekturą opartą na mikrousługach.

---

## Wymagania wstępne

Upewnij się, że masz zainstalowane:

- [Node.js](https://nodejs.org/) i npm  
- Java 17  
- [Apache Maven](https://maven.apache.org/)  
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (wraz z `kubeadm`)  
- [kubectl](https://kubernetes.io/docs/tasks/tools/)

---

##  Uruchamianie projektu

### 1. Sklonuj repozytorium

```bash
git clone https://github.com//weronikakoterba/Reservation_app
```

### 2. Uruchom wszystkie usługi

W folderze głównym projektu uruchom skrypt:

```bash
./run_all_services.sh $YOUR_DOCKERHUB_LOGIN
```
Zastąp $YOUR_DOCKERHUB_LOGIN swoim loginem do Docker Huba.

### 3. Uruchom frontend
Przejdź do folderu z frontendem (frontend-service) i wykonaj:

```bash
npm install
npm start
```

### 4. Tunelowanie portów usług (Kubernetes)
W celu lokalnego dostępu do usług uruchom następujące komendy w nowych terminalach:

```bash
kubectl port-forward -n reservation-app deployment/users-service 8080:8080
kubectl port-forward -n reservation-app deployment/reservation-service 8081:8081
kubectl port-forward -n reservation-app deployment/payment-service 8082:8082
kubectl port-forward -n reservation-app deployment/notification-service 8083:8083
```
