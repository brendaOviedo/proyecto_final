# Informe: resultado de la secuencia de comprobación

La secuencia se ejecutó partiendo de una base de datos nueva (`data/turnos.db` creada desde `sql/crear_y_cargar.sql`, 5 clientes, 3 servicios y 0 turnos). El paso 6 se cumplió cerrando el programa (opción `0. Salir`) y volviendo a abrirlo.

Las salidas completas de consola están en:

- [`evidencias/ejecucion1_pasos1-5.txt`](evidencias/ejecucion1_pasos1-5.txt): primera ejecución (pasos 1 a 5).
- [`evidencias/ejecucion2_pasos6-10.txt`](evidencias/ejecucion2_pasos6-10.txt): segunda ejecución, después de reabrir (pasos 6 a 10).

> Agreguen en la carpeta `docs/capturas/` las capturas de pantalla de su propia demostración y enlácenlas en esta sección antes de entregar.

## Resultados

| Paso | Operación | Resultado esperado | Resultado obtenido | ✔ |
|---|---|---|---|---|
| 1 | Buscar cliente 1003 mediante el BST | María García | `1003  María García  55501003  Activo` | ✔ |
| 2 | Generar turnos 1001/1, 1002/2, 1003/1, 1004/3, 1005/2 | Turnos 1 a 5 | `Turno #1` … `Turno #5` generados en ese orden | ✔ |
| 3 | Otro turno para 1001 | Rechazo | `[ERROR] El cliente 1001 ya tiene un turno pendiente.` | ✔ |
| 4 | Cancelar turno 3 | Cola: 1, 2, 4, 5 | Consulta de pendientes: 1, 2, 4, 5 | ✔ |
| 5 | Atender dos turnos | 1 y luego 2 | `Atendiendo: Turno #1 …`, `Atendiendo: Turno #2 …` | ✔ |
| 6 | Cerrar y reabrir | Cola reconstruida con 4 y 5 | `Turnos pendientes en cola: 2`; pendientes: 4, 5 | ✔ |
| 7 | Atender los restantes | 4 y luego 5 | `Atendiendo: Turno #4 …`, `Atendiendo: Turno #5 …` | ✔ |
| 8 | Atender nuevamente | Sin turnos pendientes | `[AVISO] No existen turnos pendientes.` | ✔ |
| 9 | Historial del cliente 1003 | Turno 3 cancelado | `3  1003 - María García  1 - Información  CANCELADO` | ✔ |
| 10 | Resumen por servicio | Tabla esperada | Coincide (ver abajo) | ✔ |

### Resumen por servicio obtenido (paso 10)

```
Servicio                     Pendientes  Atendidos Cancelados   Total
---------------------------------------------------------------------
Información                           0          1          1       2
Entrega de documentos                 0          2          0       2
Actualización de datos                0          1          0       1
---------------------------------------------------------------------
Total                                 0          4          1       5
```

## Validaciones comprobadas

| Caso | Mensaje del sistema |
|---|---|
| Opción inexistente (`99`) | `La opción 99 no existe.` |
| Texto en lugar de número (`abc`) | `"abc" no es un número entero válido.` |
| Entrada vacía | `El valor no puede estar vacío.` |
| Cliente no registrado (`9999`) | `No existe un cliente con el código 9999.` |
| Nombre vacío | `El campo nombre completo es obligatorio.` |
| Teléfono `123` | `El teléfono debe contener exactamente 8 dígitos.` |
| Código de cliente repetido | `Ya existe un cliente con el código 1001.` |
| Código negativo | `El código del cliente debe ser un número entero positivo.` |
| Desactivar cliente con turno pendiente | `No se puede desactivar al cliente 1006 porque tiene un turno pendiente.` |
| Desactivar servicio con turnos pendientes | `No se puede desactivar el servicio 1 porque tiene turnos pendientes.` |
| Turno para servicio inactivo | `El servicio 4 está inactivo.` |
| Turno para servicio inexistente | `No existe un servicio con el código 77.` |
| Turno para cliente inactivo | `El cliente 1006 está inactivo y no puede recibir turnos.` |
| Cancelar turno ya atendido | `El turno 1 no se puede cancelar porque está ATENDIDO.` |
| Cancelar turno inexistente | `No existe un turno con el número 999.` |

En todos los casos el programa muestra el mensaje y continúa en el menú.

Después de las pruebas, la base de datos se regeneró desde el script para entregarla con los datos iniciales y sin turnos.
