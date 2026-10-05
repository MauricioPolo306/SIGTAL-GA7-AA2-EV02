package com.mycompany.sigtal.controlador;

import com.mycompany.sigtal.dao.ClienteDAO;
import com.mycompany.sigtal.dao.VehiculoDAO;
import com.mycompany.sigtal.modelo.Cliente;
import com.mycompany.sigtal.modelo.Vehiculo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/clientes")
public class ClienteServlet extends HttpServlet {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final VehiculoDAO vehiculoDAO = new VehiculoDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if (accion == null || accion.equals("listar")) {

            listarClientes(request, response);

        } else if (accion.equals("editar")) {

            mostrarFormularioEdicion(request, response);

        } else if (accion.equals("eliminar")) {

            eliminarCliente(request, response);

        } else {

            listarClientes(request, response);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");

        if ("registrar".equals(accion)) {

            registrarCliente(request, response);

        } else if ("actualizar".equals(accion)) {

            actualizarCliente(request, response);

        } else {

            response.sendRedirect(
                    request.getContextPath() + "/clientes"
            );
        }
    }

    /**
     * Lista los clientes y, si corresponde,
     * muestra sus vehículos.
     */
    private void listarClientes(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        List<Cliente> clientes = clienteDAO.listar();

        String parametroId = request.getParameter("idCliente");

        Cliente clienteSeleccionado = null;
        List<Vehiculo> vehiculos = null;
        Vehiculo vehiculoEditar = null;

        if (parametroId != null && !parametroId.isBlank()) {

            try {

                int idCliente = Integer.parseInt(parametroId);

                clienteSeleccionado =
                        clienteDAO.buscarPorId(idCliente);

                if (clienteSeleccionado != null) {

                    vehiculos =
                            vehiculoDAO.listarPorCliente(idCliente);

                    String parametroVehiculoEditar =
                            request.getParameter("editarVehiculo");

                    if (parametroVehiculoEditar != null
                            && !parametroVehiculoEditar.isBlank()) {

                        try {

                            int idVehiculo =
                                    Integer.parseInt(parametroVehiculoEditar);

                            Vehiculo encontrado =
                                    vehiculoDAO.buscarPorId(idVehiculo);

                            if (encontrado != null
                                    && encontrado.getIdCliente() == idCliente) {

                                vehiculoEditar = encontrado;
                            }

                        } catch (NumberFormatException e) {

                            System.err.println(
                                    "ID de vehículo inválido: "
                                    + parametroVehiculoEditar
                            );
                        }
                    }
                }

            } catch (NumberFormatException e) {

                System.err.println(
                        "ID de cliente inválido: " + parametroId
                );
            }
        }

        request.setAttribute(
                "clientes",
                clientes
        );

        request.setAttribute(
                "clienteSeleccionado",
                clienteSeleccionado
        );

        request.setAttribute(
                "vehiculos",
                vehiculos
        );

        request.setAttribute(
                "vehiculoEditar",
                vehiculoEditar
        );

        request.getRequestDispatcher("/clientes.jsp")
                .forward(request, response);
    }

    /**
     * Registra un nuevo cliente y muestra
     * un mensaje específico según el resultado.
     */
    private void registrarCliente(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        Cliente cliente =
                construirClienteDesdeRequest(request);

        String resultado =
                clienteDAO.registrar(cliente);

        switch (resultado) {

            case "OK":

                response.sendRedirect(
                        request.getContextPath()
                        + "/clientes?mensaje=registrado"
                );

                break;

            case "DOCUMENTO_DUPLICADO":

                response.sendRedirect(
                        request.getContextPath()
                        + "/clientes?error=documentoDuplicado"
                );

                break;

            case "TELEFONO_DUPLICADO":

                response.sendRedirect(
                        request.getContextPath()
                        + "/clientes?error=telefonoDuplicado"
                );

                break;

            case "CORREO_DUPLICADO":

                response.sendRedirect(
                        request.getContextPath()
                        + "/clientes?error=correoDuplicado"
                );

                break;

            default:

                response.sendRedirect(
                        request.getContextPath()
                        + "/clientes?error=registro"
                );

                break;
        }
    }

    /**
     * Actualiza un cliente existente y evita
     * utilizar datos únicos pertenecientes a otro cliente.
     */
    private void actualizarCliente(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        try {

            int idCliente = Integer.parseInt(
                    request.getParameter("idCliente")
            );

            Cliente cliente =
                    construirClienteDesdeRequest(request);

            cliente.setIdCliente(idCliente);

            String resultado =
                    clienteDAO.actualizar(cliente);

            switch (resultado) {

                case "OK":

                    response.sendRedirect(
                            request.getContextPath()
                            + "/clientes?mensaje=actualizado"
                    );

                    break;

                case "DOCUMENTO_DUPLICADO":

                    response.sendRedirect(
                            request.getContextPath()
                            + "/clientes?error=documentoDuplicado"
                    );

                    break;

                case "TELEFONO_DUPLICADO":

                    response.sendRedirect(
                            request.getContextPath()
                            + "/clientes?error=telefonoDuplicado"
                    );

                    break;

                case "CORREO_DUPLICADO":

                    response.sendRedirect(
                            request.getContextPath()
                            + "/clientes?error=correoDuplicado"
                    );

                    break;

                default:

                    response.sendRedirect(
                            request.getContextPath()
                            + "/clientes?error=actualizacion"
                    );

                    break;
            }

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/clientes?error=idInvalido"
            );
        }
    }

    /**
     * Muestra el formulario para editar un cliente.
     */
    private void mostrarFormularioEdicion(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String parametroId =
                request.getParameter("id");

        if (parametroId == null
                || parametroId.isBlank()) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/clientes"
            );

            return;
        }

        try {

            int idCliente =
                    Integer.parseInt(parametroId);

            Cliente cliente =
                    clienteDAO.buscarPorId(idCliente);

            if (cliente == null) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/clientes?error=noEncontrado"
                );

                return;
            }

            request.setAttribute(
                    "clienteEditar",
                    cliente
            );

            request.getRequestDispatcher(
                    "/clientes.jsp"
            ).forward(request, response);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/clientes?error=idInvalido"
            );
        }
    }

    /**
     * Elimina un cliente.
     */
    private void eliminarCliente(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String parametroId =
                request.getParameter("id");

        if (parametroId == null
                || parametroId.isBlank()) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/clientes"
            );

            return;
        }

        try {

            int idCliente =
                    Integer.parseInt(parametroId);

            boolean eliminado =
                    clienteDAO.eliminar(idCliente);

            if (eliminado) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/clientes?mensaje=eliminado"
                );

            } else {

                response.sendRedirect(
                        request.getContextPath()
                        + "/clientes?error=eliminacion"
                );
            }

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/clientes?error=idInvalido"
            );
        }
    }

    /**
     * Construye el objeto Cliente con los datos
     * enviados desde el formulario.
     */
    private Cliente construirClienteDesdeRequest(
            HttpServletRequest request) {

        String nombre =
                request.getParameter("nombre");

        String documento =
                request.getParameter("documento");

        String telefono =
                request.getParameter("telefono");

        String correo =
                request.getParameter("correo");

        String direccion =
                request.getParameter("direccion");

        return new Cliente(
                nombre,
                documento,
                telefono,
                correo,
                direccion
        );
    }
}