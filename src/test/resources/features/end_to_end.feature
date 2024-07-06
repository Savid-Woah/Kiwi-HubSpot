Feature: Testing Step-by-Step API Flow

  Background:
    * url 'http://localhost:8080'

  Scenario: Complete Business Flow

    # Step 1: Get access token
    Given path 'kiwi/api/v1/oauth/get-access-token'
    And param code = '9c4590c0-a033-402c-8c7c-fe1d8cb9aeaf'
    When method post
    Then status 200
    And def accessToken = response.token

    # Step 2: Register a store and capture store id
    Given path 'kiwi/api/v1/auth/register-store'
    And request
      """
      {
        "user_request": {
          "email": "olimpica@gmail.com",
          "password": "olimpica444"
        },
        "store_request": {
          "name": "Olimpica",
          "domain": "www.olimpica.com",
          "city": "Barranquilla",
          "industry": "FOOD_PRODUCTION",
          "phone_number": "+57 301 3887646",
          "state": "Atlántico",
          "life_cycle_stage": "customer"
        }
      }
      """
    When method post
    Then status 200
    And def storeResponse = response
    And def store_id = storeResponse.data.store_id
    And print 'Store Registration Response:', store_id

    # Step 3: Register a Customer for that Store
    Given path 'kiwi/api/v1/auth/register-customer'
    And request
      """
      {
        "user_request": {
          "email": "savid@gmail.com",
          "password": "savid444"
        },
        "customer_request": {
          "name": "Samuel",
          "last_name": "Ballesteros",
          "phone_number": "+57 322 5447725",
          "store_id": "#(store_id)"
        }
      }
      """
    When method post
    Then status 200
    And def customerResponse = response
    And def customer_id = customerResponse.data.customer_id
    And print 'Customer Registration Response:', customer_id

    # Step 4: login
    Given path 'kiwi/api/v1/auth/login'
    And request {"email": "savid@gmail.com", "password": "savid444"}
    When method post
    Then status 200
    And def jwt = response
    And print 'Login Response:', jwt

    # Step 5: Create a contact using the registered store's information
    Given path 'kiwi/api/v1/contacts/add'
    And header Authorization = 'Bearer ' + jwt
    And request
      """
      {
        "email": "savidoficial09@gmail.com",
        "firstname": "Samuel",
        "lastname": "Ballesteros",
        "phone": "+57 322 5447725",
        "company": "Pastas La Muñeca",
        "website": "www.pastaslamuneca.com",
        "lifecyclestage": "customer",
        "store_id": "#(store_id)"
      }
      """
    When method post
    Then status 200
    And def contactResponse = response
    And def contact_id = contactResponse.data.contact_id
    And print 'Contact Creation Response:', contact_id

    # Step 6: Create a product using the store and contact information
    Given path 'kiwi/api/v1/products/add'
    And header Authorization = 'Bearer ' + jwt
    And request
      """
      {
        "name": "Spaghetti",
        "price": "5.00",
        "hs_sku": "70927823333",
        "description": "The best pasta",
        "hs_cost_of_goods_sold": "600.00",
        "hs_recurring_billing_period": "P12M",
        "store_id": "#(store_id)",
        "contact_id": "#(contact_id)",
        "stock_data": {
          "refill_quantity": 100,
          "initial_stock_quantity": 5,
          "notification_triggering_quantity": 4
        }
      }
      """
    When method post
    Then status 200
    And def productResponse = response
    And def product_id = productResponse.data.product_id
    And print 'Product Creation Response:', product_id

    # Step 7: Place an order using the
    Given path 'kiwi/api/v1/orders/place'
    And header Authorization = 'Bearer ' + jwt
    And request
      """
      {
        "hs_order_name":"Order",
        "hs_currency_code": "USD",
        "hs_source_store": "REI - Portland",
        "hs_fulfillment_status":"Packing",
        "hs_shipping_address_city":"Portland",
        "hs_shipping_address_state":"Maine",
        "hs_shipping_address_street":"123 Fake Street",
        "items_purchased": [
          {
            "product_id": "#(product_id)",
            "quantity": 1
          }
        ],
        "customer_id": "#(customer_id)"
      }
      """
    When method post
    Then status 200
    And def orderResponse = response
    And def order_id = orderResponse.data.order_id
    And print 'Order Creation Response:', order_id

    # Step 8:  Add Customer Ticket (claim)
    Given path 'kiwi/api/v1/tickets/add'
    And header Authorization = 'Bearer ' + jwt
    And request
      """
      {
        "hs_pipeline": "0",
        "hs_pipeline_stage": "1",
        "hs_ticket_priority": "HIGH",
        "subject": "Producto vencido",
        "order_id": "#(order_id)",
        "quantity": 1,
        "product_id": "#(product_id)"
      }
      """
    When method post
    Then status 200
    And def ticketResponse = response
    And def ticket_id = ticketResponse.data.ticket_id
    And print 'Ticket Creation Response:', ticket_id