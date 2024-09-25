import { z } from 'zod';

// Define tus esquemas de validación aquí
const validationSchema = z.object({
  cardNumber: z.string().min(16, 'Número de tarjeta debe tener al menos 16 dígitos'),
  expiryMonth: z.string().nonempty('Mes de expiración es requerido'),
  expiryYear: z.string().nonempty('Año de expiración es requerido'),
  cvv: z.string().length(3, 'CVV debe tener 3 dígitos'),
  cardHolderName: z.string().nonempty('Nombre del titular es requerido'),
});

// Exporta otros esquemas si es necesario
export default validationSchema