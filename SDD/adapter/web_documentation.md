# Infrastructure Layer (Web Adapters) Documentation

## Overview
The `adapter.in.web` package represents the **Primary Adapters** (Inbound). These are REST Controllers responsible for receiving HTTP requests from clients (React, Angular, Postman, etc.), mapping them to the standard Input Commands, and returning a consistent JSON response.

## Standards
- **Global Responses**: Every endpoint returns an `ApiResponse<T>` object to maintain a strict frontend contract (`{ success: true, message: "...", data: {...} }`).
- **DTOs**: Domain entities are never returned directly to the client. They are mapped to lightweight Data Transfer Objects (e.g., `UserResponseDTO`, `ProductResponseDTO`) to hide internal logic and sensitive data (like encrypted passwords or internal flags).

## Implemented Controllers

### `AuthController`
- **Endpoints**:
  - `POST /api/v1/auth/register/buyer`
  - `POST /api/v1/auth/register/seller`
- **Delegation**: Relies on `RegisterUserUseCase`.

### `ProductController`
- **Endpoints**:
  - `POST /api/v1/catalog/products`
- **Delegation**: Relies on `ManageProductUseCase` to add new physical or digital products to the seller's catalog.
