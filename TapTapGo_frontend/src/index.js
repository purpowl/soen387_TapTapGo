import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App'; //Root component

import { ProductsProvider } from './context/products_context';
import { CartProvider } from './context/cart_context';
import { UserProvider } from './context/user_context';
import { Auth0Provider } from '@auth0/auth0-react';

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
  <ProductsProvider>
    <CartProvider>
      <App />
    </CartProvider>
  </ProductsProvider>
);
