package com.mycompany.sigtal.controlador;

import com.mycompany.sigtal.dao.VehiculoDAO;
import com.mycompany.sigtal.modelo.Vehiculo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/vehiculos")
public class VehiculoServlet extends HttpServlet {

    private final VehiculoDAO vehiculoDAO =
            new VehiculoDAO();

    /*
     * ==========================================================
     * GET
     * ==========================================================
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType(
                "text/html; charset=UTF-8"
        );

        String accion =
                request.getParameter("accion");

        if ("editar".equals(accion)) {

            mostrarFormularioEdicion(
                    request,
                    response
            );

        } else if ("eliminar".equals(accion)) {

            eliminarVehiculo(
                    request,
                    response
            );

        } else {

            redirigirClientes(
                    request,
                    response
            );
        }
    }

    /*
     * ==========================================================
     * POST
     * ==========================================================
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType(
                "text/html; charset=UTF-8"
        );

        String accion =
                request.getParameter("accion");

        if ("registrar".equals(accion)) {

            registrarVehiculo(
                    request,
                    response
            );

        } else if ("actualizar".equals(accion)) {

            actualizarVehiculo(
                    request,
                    response
            );

        } else {

            redirigirClientes(
                    request,
                    response
            );
        }
    }

    /*
     * ==========================================================
     * REGISTRAR VEHÍCULO
     * ==========================================================
     */
    private void registrarVehiculo(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        try {

            Vehiculo vehiculo =
                    construirVehiculoDesdeRequest(
                            request
                    );

            String resultado =
                    vehiculoDAO.registrar(
                            vehiculo
                    );

            int idCliente =
                    vehiculo.getIdCliente();

            if ("OK".equals(resultado)) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/clientes?idCliente="
                        + idCliente
                        + "&mensaje=vehiculoRegistrado"
                );

            } else if (
                    "PLACA_DUPLICADA"
                            .equals(resultado)) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/clientes?idCliente="
                        + idCliente
                        + "&error=placaDuplicada"
                );

            } else {

                response.sendRedirect(
                        request.getContextPath()
                        + "/clientes?idCliente="
                        + idCliente
                        + "&error=registroVehiculo"
                );
            }

        } catch (NumberFormatException e) {

            String idCliente =
                    request.getParameter(
                            "idCliente"
                    );

            response.sendRedirect(
                    request.getContextPath()
                    + "/clientes?idCliente="
                    + idCliente
                    + "&error=datosVehiculo"
            );
        }
    }

    /*
     * ==========================================================
     * ACTUALIZAR VEHÍCULO
     * ==========================================================
     */
    private void actualizarVehiculo(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        try {

            int idVehiculo =
                    Integer.parseInt(
                            request.getParameter(
                                    "idVehiculo"
                            )
                    );

            Vehiculo vehiculo =
                    construirVehiculoDesdeRequest(
                            request
                    );

            vehiculo.setIdVehiculo(
                    idVehiculo
            );

            String resultado =
                    vehiculoDAO.actualizar(
                            vehiculo
                    );

            int idCliente =
                    vehiculo.getIdCliente();

            if ("OK".equals(resultado)) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/clientes?idCliente="
                        + idCliente
                        + "&mensaje=vehiculoActualizado"
                );

            } else if (
                    "PLACA_DUPLICADA"
                            .equals(resultado)) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/clientes?idCliente="
                        + idCliente
                        + "&error=placaDuplicada"
                );

            } else {

                response.sendRedirect(
                        request.getContextPath()
                        + "/clientes?idCliente="
                        + idCliente
                        + "&error=actualizacionVehiculo"
                );
            }

        } catch (NumberFormatException e) {

            String idCliente =
                    request.getParameter(
                            "idCliente"
                    );

            response.sendRedirect(
                    request.getContextPath()
                    + "/clientes?idCliente="
                    + idCliente
                    + "&error=datosVehiculo"
            );
        }
    }

    /*
     * ==========================================================
     * MOSTRAR FORMULARIO DE EDICIÓN
     * ==========================================================
     */
    private void mostrarFormularioEdicion(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String parametroId =
                request.getParameter("id");

        if (parametroId == null
                || parametroId.isBlank()) {

            redirigirClientes(
                    request,
                    response
            );

            return;
        }

        try {

            int idVehiculo =
                    Integer.parseInt(
                            parametroId
                    );

            Vehiculo vehiculo =
                    vehiculoDAO.buscarPorId(
                            idVehiculo
                    );

            if (vehiculo == null) {

                redirigirClientes(
                        request,
                        response
                );

                return;
            }

            response.sendRedirect(
                    request.getContextPath()
                    + "/clientes?idCliente="
                    + vehiculo.getIdCliente()
                    + "&editarVehiculo="
                    + vehiculo.getIdVehiculo()
            );

        } catch (NumberFormatException e) {

            redirigirClientes(
                    request,
                    response
            );
        }
    }

    /*
     * ==========================================================
     * ELIMINAR VEHÍCULO
     * ==========================================================
     */
    private void eliminarVehiculo(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String parametroId =
                request.getParameter("id");

        String parametroCliente =
                request.getParameter("idCliente");

        if (parametroId == null
                || parametroId.isBlank()) {

            redirigirClientes(
                    request,
                    response
            );

            return;
        }

        try {

            int idVehiculo =
                    Integer.parseInt(
                            parametroId
                    );

            boolean eliminado =
                    vehiculoDAO.eliminar(
                            idVehiculo
                    );

            if (parametroCliente != null
                    && !parametroCliente.isBlank()) {

                if (eliminado) {

                    response.sendRedirect(
                            request.getContextPath()
                            + "/clientes?idCliente="
                            + parametroCliente
                            + "&mensaje=vehiculoEliminado"
                    );

                } else {

                    response.sendRedirect(
                            request.getContextPath()
                            + "/clientes?idCliente="
                            + parametroCliente
                            + "&error=eliminacionVehiculo"
                    );
                }

            } else {

                redirigirClientes(
                        request,
                        response
                );
            }

        } catch (NumberFormatException e) {

            redirigirClientes(
                    request,
                    response
            );
        }
    }

    /*
     * ==========================================================
     * CONSTRUIR VEHÍCULO DESDE REQUEST
     * ==========================================================
     */
    private Vehiculo construirVehiculoDesdeRequest(
            HttpServletRequest request) {

        String placa =
                request.getParameter("placa");

        String marca =
                request.getParameter("marca");

        String modelo =
                request.getParameter("modelo");

        String anioParametro =
                request.getParameter("anio");

        String color =
                request.getParameter("color");

        String tipoVehiculo =
                request.getParameter("tipoVehiculo");

        String idClienteParametro =
                request.getParameter("idCliente");

        /*
         * ======================================================
         * LIMPIEZA DE DATOS
         * ======================================================
         */

        if (placa != null) {

            placa =
                    placa.trim()
                            .toUpperCase();
        }

        if (marca != null) {

            marca = marca.trim();
        }

        if (modelo != null) {

            modelo = modelo.trim();
        }

        if (color != null) {

            color = color.trim();
        }

        if (tipoVehiculo != null) {

            tipoVehiculo =
                    tipoVehiculo.trim();
        }

        /*
         * ======================================================
         * AÑO
         * ======================================================
         */

        Integer anio = null;

        if (anioParametro != null
                && !anioParametro.isBlank()) {

            anio =
                    Integer.parseInt(
                            anioParametro.trim()
                    );
        }

        /*
         * ======================================================
         * ID DEL CLIENTE
         * ======================================================
         */

        int idCliente =
                Integer.parseInt(
                        idClienteParametro
                );

        /*
         * ======================================================
         * CREAR OBJETO VEHÍCULO
         * ======================================================
         */

        return new Vehiculo(
                placa,
                marca,
                modelo,
                anio,
                color,
                tipoVehiculo,
                idCliente
        );
    }

    /*
     * ==========================================================
     * REDIRECCIÓN PRINCIPAL
     * ==========================================================
     */
    private void redirigirClientes(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.sendRedirect(
                request.getContextPath()
                + "/clientes"
        );
    }
}