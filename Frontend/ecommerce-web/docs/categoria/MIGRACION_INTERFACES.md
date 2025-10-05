# 🚀 Guía de Migración a Nuevas Interfaces de Categorías

## ✅ Estado Actual

La refactorización de interfaces basada en el backend **msvc-categoria** ha sido completada exitosamente:

### ✨ Nuevas Interfaces Implementadas

#### DTOs Principales
- **CategoriaDto**: 65+ campos organizados en 10 grupos funcionales
- **PagedResponse\<T>**: Paginación completa con metadatos HATEOAS
- **ApiResponse\<T>**: Wrapper de respuestas del API

#### DTOs Especializados  
- **CategoriaCreateDto**: Para crear nuevas categorías
- **CategoriaUpdateDto**: Para actualizar categorías existentes
- **CategoriaSummaryDto**: Para listados optimizados
- **CategoriaFilterDto**: Para filtros de búsqueda

#### Patrón Repository
- **CategoriaRepository**: Repositorio abstracto con todos los endpoints
- **CategoriaHttpRepository**: Implementación HTTP completa

## 📋 Compatibilidad Legacy

### ✅ Métodos Mantenidos (DEPRECATED)

Los siguientes métodos están **temporalmente mantenidos** para compatibilidad:

```typescript
// ❌ DEPRECATED - Usar obtenerCategoriasPaginadas()
getCategoriasPaginadas(page: number, size: number): Observable<PaginaCategoria>

// ❌ DEPRECATED - Usar obtenerCategoriasActivas()  
getCategorias(): Observable<Categoria[]>

// ❌ DEPRECATED - Usar obtenerCategoriaPorId()
getCategoria(id: number): Observable<Categoria>

// ❌ DEPRECATED - Usar crearCategoria()
addCategoria(categoria: Categoria): Observable<Categoria>

// ❌ DEPRECATED - Usar actualizarCategoria()
updateCategoria(id: number, categoria: Categoria): Observable<Categoria>

// ❌ DEPRECATED - Usar eliminarCategoria()
deleteCategoria(id: number): Observable<void>
```

## 🔄 Plan de Migración Gradual

### Fase 1: Nuevos Desarrollos (RECOMENDADO)
Para **nuevos componentes y funcionalidades**, usar directamente las nuevas interfaces:

```typescript
// ✅ NUEVO - Paginación completa
this.categoriaService.obtenerCategoriasPaginadas({
  page: 0,
  size: 10,
  sort: ['nombre,ASC']
}, {
  activo: true,
  texto: 'búsqueda'
}).subscribe(response => {
  this.categorias = response.content;
  this.metadata = response.page;
  this.navegacion = response.links;
});

// ✅ NUEVO - Crear categoría  
const nuevaCategoria: CategoriaCreateDto = {
  nombre: 'Nueva Categoría',
  descripcion: 'Descripción detallada',
  activo: true,
  categoriaPadreId: 1,
  tipo: TipoCategoria.PRODUCTO
};

this.categoriaService.crearCategoria(nuevaCategoria).subscribe(resultado => {
  // resultado es CategoriaDto con todos los campos
});
```

### Fase 2: Migración de Componentes Existentes (PLANIFICADO)

Los componentes actuales (`list-categoria`, `add-categoria`) **funcionan correctamente** con la compatibilidad legacy, pero se recomienda migrarlos gradualmente:

#### Ejemplo de Migración: list-categoria.component.ts

```typescript
// ANTES (Legacy - funciona pero deprecated)
this.categoriaService.getCategoriasPaginadas(this.page, this.size)

// DESPUÉS (Recomendado - nuevas capacidades)
this.categoriaService.obtenerCategoriasPaginadas({
  page: this.page,
  size: this.size,
  sort: ['nombre,ASC']
}, {
  activo: true // Solo categorías activas
})
```

## 🎯 Nuevas Capacidades Disponibles

### 🔍 Búsqueda Avanzada
```typescript
// Búsqueda por texto con filtros
this.categoriaService.buscarCategorias('electrónicos', {
  page: 0,
  size: 20
}, {
  tipo: TipoCategoria.PRODUCTO,
  activo: true,
  nivelMinimo: 1,
  nivelMaximo: 3
});
```

### 🏗️ Jerarquía de Categorías
```typescript
// Obtener subcategorías
this.categoriaService.obtenerSubcategorias(parentId, { size: 50 });

// Obtener jerarquía completa
this.categoriaService.obtenerJerarquiaCategoria(categoryId);

// Categorías raíz
this.categoriaService.obtenerCategoriasRaiz({ size: 10 });
```

### ✅ Validaciones de Negocio
```typescript
// Validar código disponible
this.categoriaService.validarCodigoDisponible('ELEC-001');

// Verificar si se puede eliminar
this.categoriaService.validarPuedeEliminar(categoryId);
```

### 📊 Operaciones Administrativas
```typescript
// Cambiar estado
this.categoriaService.cambiarEstadoCategoria(id, false, 'Temporalmente deshabilitada');

// Mover en jerarquía  
this.categoriaService.moverCategoria(childId, newParentId);

// Estadísticas
this.categoriaService.contarProductosEnCategoria(categoryId);
```

## 🛡️ Configuración del Módulo

### ✅ Ya Configurado en CategoriaCoreModule

```typescript
// El patrón Repository ya está configurado
providers: [
  CategoriaService,
  {
    provide: CATEGORIA_REPOSITORY_TOKEN,
    useClass: CategoriaHttpRepository
  }
]
```

### 📦 Importación en app.module.ts

```typescript
import { CategoriaCoreModule } from './categoria/core';

@NgModule({
  imports: [
    // Otros imports...
    CategoriaCoreModule.forRoot() // Una sola vez en el root
  ]
})
export class AppModule { }
```

## ⚡ Rendimiento y Caching

Las nuevas interfaces incluyen soporte para:
- **Paginación eficiente** con metadatos completos
- **HATEOAS links** para navegación
- **Filtros granulares** para reducir transferencia de datos
- **DTOs especializados** por caso de uso (Summary, Create, Update)

## 🎯 Próximos Pasos Recomendados

### Inmediatos (Sin Prisa)
1. ✅ **Mantener funcionamiento actual** - Todo funciona correctamente
2. ✅ **Usar nuevas interfaces** para nuevos desarrollos
3. ✅ **Importar CategoriaCoreModule.forRoot()** en AppModule

### A Mediano Plazo (Opcional)
1. 🔄 **Migrar list-categoria** para aprovechar filtros avanzados
2. 🔄 **Migrar add-categoria** para usar CategoriaCreateDto/UpdateDto  
3. 🔄 **Implementar funcionalidades jerarquía** y validaciones
4. 🔄 **Agregar búsqueda avanzada** y operaciones administrativas

### A Largo Plazo (Recomendado)
1. 🗑️ **Remover métodos deprecated** (getCategoriasPaginadas, etc.)
2. 🧹 **Limpiar interfaces legacy** (Categoria, PaginaCategoria)
3. 🎯 **Aprovechar todas las capacidades** del backend empresarial

## 🚫 Breaking Changes

**NINGÚN BREAKING CHANGE** - Toda la funcionalidad existente sigue funcionando exactamente igual.

La refactorización es **100% backwards compatible** y **no requiere cambios inmediatos**.

---

## 📞 Soporte

Para cualquier duda sobre la migración o uso de las nuevas interfaces, revisar:
- `categoria/core/models/` - Todas las nuevas interfaces
- `categoria/core/services/categoria.service.ts` - Métodos disponibles
- `categoria/core/repositories/` - Patrón Repository implementado
