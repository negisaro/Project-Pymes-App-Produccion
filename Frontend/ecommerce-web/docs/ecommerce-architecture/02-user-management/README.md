# 👤 User Management - Gestión de Usuarios

## 📋 Descripción

Sistema completo de gestión de usuarios, autenticación, autorización y perfiles.

## 🎯 Componentes Principales

### 🔐 Authentication
- **Login Component** - Formulario de inicio de sesión
- **Register Component** - Registro de nuevos usuarios
- **Password Recovery** - Recuperación de contraseña
- **Two-Factor Auth** - Autenticación de dos factores
- **Social Login** - Login con redes sociales

### 👨‍💼 User Profiles
- **Profile Management** - Gestión de perfil personal
- **Address Book** - Libro de direcciones
- **Order History** - Historial de pedidos
- **Wishlist** - Lista de deseos
- **Preferences** - Preferencias del usuario

### 🛡️ Authorization
- **Role Management** - Gestión de roles
- **Permission System** - Sistema de permisos
- **Access Control** - Control de acceso
- **Admin Users** - Usuarios administradores
- **Customer Roles** - Roles de clientes

### 💬 User Communication
- **User Notifications** - Notificaciones personales
- **Message Center** - Centro de mensajes
- **Support Tickets** - Tickets de soporte
- **Communication Preferences** - Preferencias de comunicación

## 📁 Estructura de Archivos

```
src/app/user/
├── auth/
│   ├── login/
│   ├── register/
│   ├── forgot-password/
│   └── two-factor/
├── profile/
│   ├── personal-info/
│   ├── address-book/
│   ├── security-settings/
│   └── preferences/
├── dashboard/
│   ├── customer-dashboard/
│   ├── order-history/
│   ├── wishlist/
│   └── notifications/
├── admin/
│   ├── user-list/
│   ├── role-management/
│   └── permissions/
└── shared/
    ├── user.service.ts
    ├── auth.service.ts
    └── user.interfaces.ts
```

## 🚀 Estado Actual vs Objetivo

### ✅ Implementado
- [x] Login básico
- [x] Registro de usuarios
- [x] Guards de autenticación
- [x] Perfil básico

### 🔄 En Desarrollo
- [ ] Dashboard de usuario completo
- [ ] Gestión de direcciones
- [ ] Sistema de roles avanzado

### 📋 Pendiente
- [ ] Two-factor authentication
- [ ] Social login
- [ ] Sistema de tickets
- [ ] Notificaciones push
- [ ] Preferencias avanzadas

## 🔗 Dependencias

- **@angular/forms** - Formularios reactivos
- **@auth0/angular-jwt** - Manejo de JWT
- **ngx-toastr** - Notificaciones
- **@angular/material** - Componentes UI

## 📖 Guías

- [Implementación de Auth](./auth-implementation.md)
- [Gestión de Roles](./role-management.md)
- [User Dashboard](./dashboard-guide.md)
- [Security Patterns](./security-patterns.md)
