# Suppliers Module

## Purpose
Manage supplier entities (listing, creation, editing, deletion) within the admin dashboard. Suppliers are companies or individuals that provide products to the business.

## Current Structure
```
features/suppliers/
├── suppliers.module.ts
├── suppliers-routing.module.ts
├── interfaces/
│   └── supplier.interface.ts
├── services/
│   └── supplier.service.ts
└── pages/
    ├── suppliers-list/
    │   ├── suppliers-list.component.ts
    │   ├── suppliers-list.component.html
    │   └── suppliers-list.component.css
    └── supplier-form/
        ├── supplier-form.component.ts
        ├── supplier-form.component.html
        └── supplier-form.component.css
```

## Features
- **Complete CRUD Operations**: Create, Read, Update, Delete suppliers
- **Advanced Listing**: Pagination, search by name, filter by status
- **Reactive Forms**: Full form validation with error handling
- **Modern UX**: SweetAlert2 notifications and confirmations
- **Search Capabilities**: Find suppliers by name or filter by product
- **Status Management**: Active/Inactive supplier states
- **Responsive Design**: Mobile-friendly interface

## Interfaces
```typescript
interface Supplier {
  id?: number;
  name: string;
  description: string;
  contact?: string;
  active: boolean;
  createdAt?: string;
  updatedAt?: string;
}
```

## Service Methods
- `getSuppliers(page, size)` - Paginated supplier list
- `getSupplier(id)` - Get single supplier
- `getActiveSuppliers()` - Filter active suppliers only
- `searchByName(name)` - Search by supplier name
- `getSuppliersByProduct(productId)` - Find suppliers for specific product
- `createSupplier(supplier)` - Create new supplier
- `updateSupplier(id, supplier)` - Update existing supplier
- `deleteSupplier(id)` - Delete supplier

## Routes
```
/admin/dashboard-admin/suppliers            -> SuppliersListComponent
/admin/dashboard-admin/suppliers/add        -> SupplierFormComponent (create)
/admin/dashboard-admin/suppliers/edit/:id   -> SupplierFormComponent (edit)
```

## Key Features
- **No Custom Layout**: Uses global admin layout for consistency
- **Full Translation**: All text in English for international standards
- **Advanced Validation**: Required fields, max length validation
- **Error Handling**: Comprehensive error management with user feedback
- **Loading States**: Visual feedback during API operations
- **Confirmation Dialogs**: SweetAlert2 for delete confirmations
- **Toast Notifications**: Non-intrusive success/error messages

## Dependencies
- **ReactiveFormsModule**: For form handling
- **SharedModule**: Common components and utilities
- **SweetAlert2**: Enhanced user notifications
- **FontAwesome**: Modern icon library

## API Integration
All service methods connect to the existing backend endpoints:
- Base URL: `${environment.baseUrl}/api/segura/proveedores/`
- Maintains backward compatibility with existing API structure
- Proper error handling and loading states

## Styling
- **Bootstrap-based**: Responsive design system
- **Custom CSS**: Enhanced styling for better UX
- **Mobile-first**: Responsive breakpoints for all devices
- **Consistent Design**: Follows app-wide design patterns

---
Created: October 3, 2025 - Automated refactoring from `proveedor` to `suppliers`