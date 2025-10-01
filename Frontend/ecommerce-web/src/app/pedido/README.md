# Módulo Pedido

## Propósito
Gestionar entidades de pedidos (listado, detalle, creación, actualización, cambio de estado) dentro del dashboard admin.

## Estructura actual
```
pedido/
  pedido.module.ts
  pedido-routing.module.ts
  pedido-layout/
    pedido-layout.component.*
  pages/
    list-pedido/
      list-pedido.component.*
```

## Convenciones
- Carpeta `pages/` agrupa componentes de vista (list, form, detail).
- Nombre de componentes: `ListPedidoComponent`, `FormPedidoComponent`, `DetailPedidoComponent`.
- Layout principal: `PedidoLayoutComponent` sirve como wrapper para subsecciones.

## Próximas extensiones sugeridas
1. Formulario
   - `pages/form-pedido/` + rutas: `add`, `edit/:id`.
2. Detalle
   - `pages/detail-pedido/` + ruta: `detail/:id`.
3. Servicio
   - `service/pedido.service.ts` con métodos CRUD y cambios de estado.
4. Interfaces
   - `interfaces/pedido.interface.ts` con tipos: `Pedido`, `PedidoEstado`, `PedidoLinea`.
5. Estado global (opcional)
   - NgRx/Signals para cachear pedidos y evitar refetch.

## Rutas previstas
```
/admin/dashboard-admin/pedido            -> ListPedidoComponent
/admin/dashboard-admin/pedido/add        -> FormPedidoComponent (crear)
/admin/dashboard-admin/pedido/edit/:id   -> FormPedidoComponent (editar)
/admin/dashboard-admin/pedido/detail/:id -> DetailPedidoComponent (detalle)
```

## Pipes y Localización
`PedidoModule` importa `CommonModule`, lo que habilita las pipes `date` y `currency`.
La localización global está configurada en `AppModule` (`LOCALE_ID` = `es-CO`).

## Notas
- Evitar mantener componentes duplicados (se eliminó la versión antigua `pedido-list.component.*`).
- Para tablas grandes considerar paginación reutilizable (componente shared).
- Añadir accesibilidad: `aria-live` para acciones de guardado/cambio de estado.

---
Última actualización: automatizada por asistente.
