```mermaid
sequenceDiagram
  autonumber
  participant U as Usuario
  participant APIG as API Gateway
  participant ALB as ALB
  participant AUTH as Auth Service
  participant COG as Cognito
  participant CAT as Catalog Service
  participant ORD as Orders Service
  participant RDS as RDS (BD)
  participant S3 as S3 (Objetos)

  Note over U,APIG: Inicio de sesión
  U->>APIG: POST /login (credenciales)
  APIG->>ALB: /auth/login
  ALB->>AUTH: /login
  AUTH->>COG: Validar/emitir JWT
  COG-->>AUTH: JWT válido
  AUTH-->>ALB: 200 (JWT)
  ALB-->>APIG: 200 (JWT)
  APIG-->>U: 200 (JWT)

  Note over U,APIG: Consulta de catálogo
  U->>APIG: GET /catalog (Bearer JWT)
  APIG->>ALB: /catalog
  ALB->>CAT: /catalog
  CAT->>RDS: SELECT productos
  CAT->>S3: Obtener URLs de imágenes
  CAT-->>ALB: 200 (lista productos + URLs)
  ALB-->>APIG: 200
  APIG-->>U: 200 (JSON productos)

  Note over U,APIG: Crear pedido
  U->>APIG: POST /orders (Bearer JWT, carrito)
  APIG->>ALB: /orders
  ALB->>ORD: /orders
  ORD->>RDS: INSERT pedido + UPDATE stock (tx local)
  ORD-->>ALB: 201 (confirmación)
  ALB-->>APIG: 201
  APIG-->>U: 201 (nº de pedido)
