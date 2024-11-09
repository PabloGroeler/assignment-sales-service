package com.challenge.controller;

import com.challenge.services.OrderService;
import com.challenge.utils.ControllerTestResources;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.challenge.utils.AppConstants.VIEW_ORDERS_PERMISSION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest extends ControllerTestResources {

  @MockBean
  public OrderService orderService;

  @Test
  @DisplayName("Makes sure that an admin can retrieve orders.")
  void happyAdminRetrieveOrders() throws Exception {
    var token = createDefaultAdminAuthToken();

    this.mockedMvc.perform(get("/api/v1/orders")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Makes sure that an user with the proper permission can retrieve orders.")
  void happyOrderRetrieveOrders() throws Exception {
    var token = createDefaultAuthToken(VIEW_ORDERS_PERMISSION);

    this.mockedMvc.perform(get("/api/v1/orders")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Makes sure that an user without proper permissions is denied.")
  void unhappyOrdersRetrieveOrders() throws Exception {
    var token = createDefaultAuthToken();

    this.mockedMvc.perform(get("/api/v1/orders")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isForbidden());
  }
}
