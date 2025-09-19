```mermaid
flowchart LR
  %% Límites
  subgraph Internet [Internet]
    U[Usuario Web/App]
  end

  subgraph Publico [Zona pública]
    APIG[API Gateway]
    ALB[ALB (Balanceador)]
  end

  subgraph VPCPrivada [VPC - Zona privada]
    subgraph ECSCluster [ECS (Fargate)]
      AUTH[Auth Service]
      CAT[Catalog Service]
      ORD[Orders Service]
    end
    RDS[(RDS - BD)]
  end

  subgraph ServiciosAWS [Servicios Gestionados]
    S3[(S3 - Objetos)]
    COG[Cognito (Identidad)]
    CWA[CloudWatch (Logs/Métricas)]
    ECR[(ECR - Imágenes)]
  end

  %% Flujo principal
  U -->|HTTPS| APIG -->|HTTP/HTTPS| ALB
  ALB -->|/auth| AUTH
  ALB -->|/catalog| CAT
  ALB -->|/orders| ORD

  %% Dependencias
  AUTH -->|Validación/Emisión JWT| COG
  CAT -->|Lecturas/Escrituras| RDS
  CAT -->|Imágenes| S3
  ORD -->|Transacciones| RDS

  %% Observabilidad
  AUTH --> CWA
  CAT --> CWA
  ORD --> CWA
  ALB --> CWA
  APIG --> CWA

  %% Despliegue
  ECR -.->|Docker pull| AUTH
  ECR -.->|Docker pull| CAT
  ECR -.->|Docker pull| ORD
```


