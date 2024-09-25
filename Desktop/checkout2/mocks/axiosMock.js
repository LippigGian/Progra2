import axios from "../axiosConfig"
import MockAdapter from 'axios-mock-adapter';

// Crear una instancia de MockAdapter
// const mock = new MockAdapter(axios, );
const mock = new MockAdapter(axios, { delayResponse: 2000 });
// Configurar los mocks
//Mock para obtener el detalle de una orden por path param
mock.onGet(/\/orders\/\w+/).reply((config) => {
  // Extrae el orderId del URL
  const orderId = config.url.split('/').pop();

  // Define una respuesta simulada basada en el orderId
  const mockData = {
    id: orderId,
    description: 'Descripción de ejemplo',
    amount: 100,
    name: 'Gianfranco Lippi',
  };

  return [200, mockData];
});
mock.onGet('/users').reply(200, {
  users: [{ id: 1, name: 'John Smith' }],
});

mock.onPost('/api/data').reply(200, {
  link: 'https://mocked-payment-link.com',
});

// mock.onPost('/api/link').reply(200, {
//   message: 'User registered successfully',
// });

// Configurar los mocks
mock.onPost('/api/link').reply(config => {
  // Parsear los datos enviados en el cuerpo de la petición
  const { amount, description, currencyCode, userId } = JSON.parse(config.data);

  // Devolver una respuesta que incluya los datos dinámicos
  return [200, {
    amount: amount,
    description: description,
    currencyCode: currencyCode,
    userId: userId,
    message: `Se creó el link de pago por $${amount} para "${description}"`,
    link: 'https://mocked-payment-link.com',
  }];
});
mock.onPost('/login').reply(200, {
  token: 'Gian el fatheeeeeeer',
});


mock.onPost('app/login').reply(200, {
  token: 'Gian el fatheeeeeeer',
});


mock.onPost('/api/numerotarjeta').reply(200, {
  
    "success": "OK",
    "data": [
      {
        "name": "Visa",
        "brand": "VISA",
        "type": "CREDIT",
        "securityCodeLength": 3,
        "imageUrl": "https://geopagos.s3.amazonaws.com/images/cabal/payments/visa.png",
        "installments": [
          {
            "code": null,
            "financialRate": 0,
            "installment": 1,
            "installmentId": 31,
            "name": "1 Cuota",
            "quantity": 1,
            "receivedAmount": 9.41,
            "total": 10,
            "type": "INSTALLMENT_WITH_FC"
          },
          {
            "code": null,
            "financialRate": 0.1444,
            "installment": 3,
            "installmentId": 203,
            "name": "3 Cuotas",
            "quantity": 3,
            "receivedAmount": 9.29,
            "total": 12.12,
            "type": "INSTALLMENT_WITH_FC"
          },
          {
            "code": null,
            "financialRate": 0,
            "installment": 6,
            "installmentId": 204,
            "name": "6 Cuotas",
            "quantity": 6,
            "receivedAmount": 6.57,
            "total": 10,
            "type": "INSTALLMENT_WITH_FC"
          },
          {
            "code": null,
            "financialRate": 0,
            "installment": 9,
            "installmentId": 32,
            "name": "9 Cuotas",
            "quantity": 9,
            "receivedAmount": 5.49,
            "total": 10,
            "type": "INSTALLMENT_WITH_FC"
          },
          {
            "code": null,
            "financialRate": 0.392,
            "installment": 12,
            "installmentId": 34,
            "name": "12 Cuotas",
            "quantity": 12,
            "receivedAmount": 8.86,
            "total": 19.02,
            "type": "INSTALLMENT_WITH_FC"
          },
          {
            "code": null,
            "financialRate": 0.0603,
            "installment": 13,
            "installmentId": 33,
            "name": "Cuota Simple 3",
            "quantity": 13,
            "receivedAmount": 9.36,
            "total": 10.79,
            "type": "INSTALLMENT_WITH_FC"
          },
          {
            "code": null,
            "financialRate": 0.1145,
            "installment": 16,
            "installmentId": 35,
            "name": "Cuota Simple 6",
            "quantity": 16,
            "receivedAmount": 9.31,
            "total": 11.61,
            "type": "INSTALLMENT_WITH_FC"
          },
          {
            "code": null,
            "financialRate": 0.1645,
            "installment": 19,
            "installmentId": 985,
            "name": "Cuota Simple 9",
            "quantity": 19,
            "receivedAmount": 9.27,
            "total": 12.49,
            "type": "INSTALLMENT_WITH_FC"
          },
          {
            "code": null,
            "financialRate": 0.2107,
            "installment": 7,
            "installmentId": 961,
            "name": "Cuota Simple 12",
            "quantity": 7,
            "receivedAmount": 9.2,
            "total": 13.42,
            "type": "INSTALLMENT_WITH_FC"
          }
        ],
        "merchantId": "77000170",
        "terminalId": "77000170"
      }
    ]
  
});

export default mock;




