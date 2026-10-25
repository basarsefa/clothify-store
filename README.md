# Clothify Store

Clothify Store is a full-stack e-commerce application built with React and Spring Boot microservices. The backend services run on Kubernetes, while the React frontend runs locally during development.

## Architecture

```text
React Frontend (localhost:3000)
              |
              v
        API Gateway
        (Kubernetes)
              |
       +------+------+
       |             |
       v             v
 Auth Service   Category Service
       |             |
       v             v
 Auth PostgreSQL  Category PostgreSQL
```

Centralized logging is provided by Grafana Alloy, Loki, and Grafana. NGINX Ingress is used to route HTTP requests to the API Gateway and Grafana.

## Technologies

- **Frontend:** React
- **Backend:** Java, Spring Boot
- **API Gateway:** Spring Cloud Gateway
- **Authentication:** JWT
- **Databases:** PostgreSQL
- **Containerization:** Docker
- **Orchestration:** Kubernetes with Minikube
- **Ingress:** NGINX Ingress Controller
- **Logging:** Grafana Alloy, Loki, Grafana
- **Package management:** Helm

## Project Structure

```text
clothify-store/
├── frontend/
├── backend/
│   ├── api-gateway/
│   ├── auth-service/
│   └── cetegory-service/
├── infrastructure/
│   ├── kubernetes/
│   └── monitoring/
├── .gitignore
└── README.md
```

## Development Setup

The React frontend runs locally at `http://localhost:3000`. The backend services and supporting infrastructure run inside a local Kubernetes cluster managed by Minikube.

Kubernetes manifests are located in `infrastructure/kubernetes/`, and monitoring Helm values are stored in `infrastructure/monitoring/`.

> Configuration and startup commands will be documented after verifying the required environment variables, Kubernetes resources, and Helm releases.

## Prerequisites

Make sure the following tools are installed:

- Git
- Docker Desktop
- Minikube
- kubectl
- Helm
- Node.js and npm

## Starting the Local Environment

Start Docker Desktop and make sure the Docker engine is running.

Start the Minikube cluster:

```bash
minikube start
```

Verify that the Kubernetes cluster is available:

```bash
kubectl get nodes
```

Check the status of the deployed workloads:

```bash
kubectl get pods -A
```

List the installed Helm releases:

```bash
helm list -A
```

## Deploying the Backend to Kubernetes

The Kubernetes manifests are located in `infrastructure/kubernetes/`.

### 1. Configure Kubernetes Secrets

Before deploying the application, create the actual Secret manifests from the provided example files.

Copy each example file to its corresponding Secret manifest filename, then replace the placeholder values with your own configuration. Keep actual Secret files private and never commit real credentials to Git.

### 2. Create the Database Resources

```bash
kubectl apply -f infrastructure/kubernetes/databases/auth-postgres/secret.yaml
kubectl apply -f infrastructure/kubernetes/databases/auth-postgres/service.yaml
kubectl apply -f infrastructure/kubernetes/databases/auth-postgres/statefulset.yaml

kubectl apply -f infrastructure/kubernetes/databases/category-postgres/secret.yaml
kubectl apply -f infrastructure/kubernetes/databases/category-postgres/service.yaml
kubectl apply -f infrastructure/kubernetes/databases/category-postgres/statefulset.yaml
```

### 3. Deploy the Auth Service

```bash
kubectl apply -f infrastructure/kubernetes/auth/auth-configmap.yaml
kubectl apply -f infrastructure/kubernetes/auth/auth-secret.yaml
kubectl apply -f infrastructure/kubernetes/auth/auth-service.yaml
kubectl apply -f infrastructure/kubernetes/auth/auth-deployment.yaml
```

### 4. Deploy the Category Service

```bash
kubectl apply -f infrastructure/kubernetes/category/category-configmap.yaml
kubectl apply -f infrastructure/kubernetes/category/category-secret.yaml
kubectl apply -f infrastructure/kubernetes/category/category-service.yaml
kubectl apply -f infrastructure/kubernetes/category/category-deployment.yaml
```

### 5. Deploy the API Gateway

```bash
kubectl apply -f infrastructure/kubernetes/gateway/gateway-configmap.yaml
kubectl apply -f infrastructure/kubernetes/gateway/gateway-secret.yaml
kubectl apply -f infrastructure/kubernetes/gateway/gateway-service.yaml
kubectl apply -f infrastructure/kubernetes/gateway/gateway-deployment.yaml
```

### 6. Configure Ingress

```bash
kubectl apply -f infrastructure/kubernetes/ingress/ingress.yaml
```

### 7. Verify the Deployment

```bash
kubectl get pods
kubectl get services
kubectl get ingress
```

## Monitoring and Centralized Logging

Clothify Store uses Grafana Alloy to collect Kubernetes pod logs, Loki to store and query logs, and Grafana to visualize them.

### 1. Install Loki

```bash
helm upgrade --install loki grafana-community/loki \
  --namespace monitoring \
  --create-namespace \
  --values infrastructure/monitoring/loki/loki-values.yaml
```

### 2. Install Grafana Alloy

```bash
helm upgrade --install alloy grafana/alloy \
  --namespace monitoring \
  --values infrastructure/monitoring/alloy/alloy-values.yaml
```

### 3. Install Grafana

```bash
helm upgrade --install grafana grafana/grafana \
  --namespace monitoring
```

Make sure the required Helm repositories are configured before running these commands.

### 4. Verify Monitoring Components

```bash
helm list -n monitoring
kubectl get pods -n monitoring
```

### 5. Explore Logs in Grafana

Open Grafana through the configured Ingress host:

`http://grafana.clothify.local`

In Grafana, open **Explore** and select the Loki data source.

Example LogQL query for backend service logs:

```logql
{instance=~".*(api-gateway|auth-service|category-service).*"}
| json
```

To display warning-level logs:

```logql
{instance=~".*(api-gateway|auth-service|category-service).*"}
| json
| level="WARN"
```

> **Note:** This monitoring setup is intended for local development. Loki persistence is disabled in the current configuration, so logs may be lost when Loki is recreated.

## Running the Frontend

The React frontend runs locally during development, while the backend services run in Kubernetes.

Navigate to the frontend directory:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Configure the frontend API base URL in `frontend/.env`:

```env
REACT_APP_API_URL=http://clothify.local/api/v1
```

Start the development server:

```bash
npm start
```

The frontend is available at:

http://localhost:3000

Make sure Minikube, the Ingress controller, and the required backend services are running before using the application.

## Application Access

The local development environment uses the following endpoints:

| Component      | Address                       |
| -------------- | ----------------------------- |
| React Frontend | http://localhost:3000         |
| API Gateway    | http://clothify.local         |
| Grafana        | http://grafana.clothify.local |

The hostnames `clothify.local` and `grafana.clothify.local` must resolve to the local machine. Configure your Windows hosts file if necessary:

```text
127.0.0.1 clothify.local
127.0.0.1 grafana.clothify.local
```

The Ingress controller must be running and accessible from the host machine.

### Accessing Ingress on Windows with Minikube

When using Minikube with the Docker driver on Windows, additional port forwarding may be required to access the Ingress controller.

You can expose the Ingress controller locally by running:

```bash
minikube service ingress-nginx-controller -n ingress-nginx --url
```

Keep the terminal running while accessing the application. The command creates a local forwarding address, and the forwarding process may stop when the command is interrupted or the terminal is closed. The generated local port may also change when the command is run again.

If you configure Windows port forwarding with `netsh interface portproxy`, make sure its destination port matches the currently active local forwarding port.

Protected API endpoints require a valid JWT access token.

## Notes

- Kubernetes Secret manifests contain sensitive configuration and must not be committed to the repository.
- PostgreSQL data is stored in PersistentVolumeClaims.
- The frontend runs locally during development; it is not deployed to Kubernetes.
- The current setup is intended for local development and learning, not production deployment.
