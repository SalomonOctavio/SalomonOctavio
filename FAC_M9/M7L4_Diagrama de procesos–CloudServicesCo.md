```mermaid
graph LR
  U[Usuario Web/App] -->|HTTPS| APIG[API Gateway]
  APIG -->|HTTP/HTTPS| ALB[ALB Balanceador]

  ALB -->|/auth| AUTH[Auth Service]
  ALB -->|/catalog| CAT[Catalog Service]
  ALB -->|/orders| ORD[Orders Service]

  AUTH -->|JWT| COG[Cognito]
  CAT -->|Lecturas/Escrituras| RDS[(RDS - BD)]
  CAT -->|Imágenes| S3[(S3 - Objetos)]
  ORD -->|Transacciones| RDS

  %% Observabilidad
  AUTH --> CWA[CloudWatch]
  CAT --> CWA
  ORD --> CWA
  ALB --> CWA
  APIG --> CWA

  %% Despliegue
  ECR[(ECR - Imágenes)] -.->|Docker pull| AUTH
  ECR -.->|Docker pull| CAT
  ECR -.->|Docker pull| ORD