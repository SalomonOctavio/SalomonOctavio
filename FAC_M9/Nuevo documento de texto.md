graph LR
  %% Punto 4 - Arquitectura de integracion y simulacion (seguridad automatizada)

  ORG[AWS Organizations]

  SECHUB[Seguridad: Security Hub (Agregador)]
  SECGD[Seguridad: GuardDuty (Master)]
  SECAGG[Seguridad: AWS Config Aggregator]
  SECEB[Seguridad: EventBridge (Bus central)]
  SECLMB[Seguridad: Lambda Runbooks]
  SECAUD[Seguridad: Auditoria S3/Glacier]

  A1SVC[App A: ALB/CloudFront, EC2/EKS/Lambda, S3]
  A1CFG[App A: AWS Config]
  A1SH[App A: Security Hub (Miembro)]
  A1GD[App A: GuardDuty (Miembro)]
  A1EB[App A: EventBridge Reglas]
  A1MAC[App A: Macie]
  A1KMS[App A: KMS CMK]
  A1CW[App A: CloudWatch]

  A2SVC[App B: ALB/CloudFront, EC2/EKS/Lambda, S3]
  A2CFG[App B: AWS Config]
  A2SH[App B: Security Hub (Miembro)]
  A2GD[App B: GuardDuty (Miembro)]
  A2EB[App B: EventBridge Reglas]
  A2MAC[App B: Macie]
  A2KMS[App B: KMS CMK]
  A2CW[App B: CloudWatch]

  %% Organizacion y membresias
  ORG --> SECHUB
  ORG --> SECGD
  ORG --> SECAGG

  %% Postura y cumplimiento
  A1CFG --> SECAGG
  A2CFG --> SECAGG
  A1SH --> SECHUB
  A2SH --> SECHUB
  A1GD --> SECGD
  A2GD --> SECGD

  %% Deteccion y respuesta automatica (por cuenta)
  A1CFG -- No compliance --> A1EB
  A1GD -- Hallazgo de amenaza --> A1EB
  A1EB --> SECLMB

  A2CFG -- No compliance --> A2EB
  A2GD -- Hallazgo de amenaza --> A2EB
  A2EB --> SECLMB

  %% Orquestacion central (opcional)
  SECHUB -- Hallazgos agregados --> SECEB
  SECEB --> SECLMB

  %% Remediaciones tipicas
  SECLMB -- Cerrar bucket publico / forzar SSE-KMS --> A1SVC
  SECLMB -- Rotar claves / aplicar policies --> A1KMS
  SECLMB -- Cambios de seguridad en servicios --> A1SVC

  SECLMB -- Cerrar bucket publico / forzar SSE-KMS --> A2SVC
  SECLMB -- Rotar claves / aplicar policies --> A2KMS
  SECLMB -- Cambios de seguridad en servicios --> A2SVC

  %% Evidencias centralizadas
  SECAGG --> SECAUD
  SECHUB --> SECAUD
  SECGD --> SECAUD
  A1CW --> SECAUD
  A2CW --> SECAUD
