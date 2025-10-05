# 🏛️ Core System - Sistema Principal

## 📋 Descripción

Componentes fundamentales y base arquitectural del sistema E-Commerce.

## 🎯 Componentes Principales

### 🔧 Infrastructure
- **Routing System** - Navegación y rutas de la aplicación
- **State Management** - Gestión global del estado (NgRx/Akita)
- **HTTP Client** - Cliente HTTP para APIs
- **Error Handling** - Manejo global de errores
- **Loading States** - Estados de carga globales

### 🎨 UI Foundation
- **Design System** - Tokens de diseño y variables CSS
- **Theme Engine** - Sistema de temas (light/dark)
- **Layout Components** - Layouts base (header, footer, sidebar)
- **Typography System** - Sistema tipográfico
- **Icon Library** - Librería de iconos

### 🛡️ Security & Guards
- **Authentication Guards** - Protección de rutas
- **Role-based Access** - Control de acceso por roles
- **JWT Management** - Manejo de tokens JWT
- **Security Headers** - Headers de seguridad

### 📱 Progressive Web App
- **Service Worker** - Funcionalidad offline
- **App Manifest** - Configuración PWA
- **Push Notifications** - Notificaciones push
- **Caching Strategy** - Estrategia de cache

## 📁 Estructura de Archivos

```
src/app/core/
├── guards/
│   ├── auth.guard.ts
│   ├── role.guard.ts
│   └── permission.guard.ts
├── interceptors/
│   ├── auth.interceptor.ts
│   ├── error.interceptor.ts
│   └── loading.interceptor.ts
├── services/
│   ├── auth.service.ts
│   ├── storage.service.ts
│   ├── notification.service.ts
│   └── theme.service.ts
├── models/
│   ├── user.interface.ts
│   ├── api-response.interface.ts
│   └── common.interfaces.ts
└── constants/
    ├── api-endpoints.ts
    ├── app-config.ts
    └── storage-keys.ts
```

## 🚀 Estado Actual vs Objetivo

### ✅ Implementado
- [x] Routing básico
- [x] Guards de autenticación
- [x] Interceptores HTTP
- [x] Servicios básicos

### 🔄 En Desarrollo
- [ ] State management completo
- [ ] Service Worker
- [ ] Theme engine avanzado

### 📋 Pendiente
- [ ] Push notifications
- [ ] Error boundaries
- [ ] Performance monitoring
- [ ] A/B testing framework

## 🔗 Dependencias

- **Angular Router** - Navegación
- **RxJS** - Programación reactiva
- **NgRx** (opcional) - State management
- **Angular PWA** - Progressive Web App

## 📖 Guías

- [Configuración de Guards](./guards-setup.md)
- [Manejo de Estados](./state-management.md)
- [Configuración PWA](./pwa-setup.md)
- [Security Best Practices](./security-guide.md)
