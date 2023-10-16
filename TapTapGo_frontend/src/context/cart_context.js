import React, { useEffect, useContext, useReducer } from 'react';
import reducer from '../reducers/cart_reducer';
import {
  ADD_TO_CART,
  REMOVE_CART_ITEM,
  TOGGLE_CART_ITEM_AMOUNT,
  COUNT_CART_TOTALS,
} from '../actions';

// Check if there already exist a cart item in local storage.
// If yes, set the default cart in initialState to the cart in local storage.
// If no, create an empty array
const getLocalStorage = () => {
  let cart = localStorage.getItem('cart');
  if (cart) {
    return JSON.parse(localStorage.getItem('cart'));
  } else {
    return [];
  }
};
const initialState = {
  cart: getLocalStorage(), // By doing this, we won't loose the products inside cart when we refresh the page
  total_items: 0,
  total_amount: 0,
  shipping_fee: 560,
};

const CartContext = React.createContext();

export const CartProvider = ({ children }) => {
  const [state, dispatch] = useReducer(reducer, initialState);

  // add to cart
  const addToCart = (id, amount, product) => {
    dispatch({ type: ADD_TO_CART, payload: { id, amount, product } });
  };

  // remove item from cart
  const removeItem = (id) => {
    dispatch({ type: REMOVE_CART_ITEM, payload: id });
  };

  // toggle amount
  const toggleAmount = (id, value) => {
    dispatch({ type: TOGGLE_CART_ITEM_AMOUNT, payload: { id, value } });
  };

  // When user add products to cart, the products are temporarily store in local storage
  // Every time we refresh, or make a change, we'll start from scratch since the cart is an empty array by default
  // To prevent such thing, we set the default value of cart to getLocalStorage()
  useEffect(() => {
    // Everytime the cart is updated, the total_items value is increase/decrease
    dispatch({ type: COUNT_CART_TOTALS });
    // Local storage only store string value
    localStorage.setItem('cart', JSON.stringify(state.cart));
  }, [state.cart]);

  return (
    <CartContext.Provider
      value={{ ...state, addToCart, removeItem, toggleAmount }}
    >
      {children}
    </CartContext.Provider>
  );
};
// make sure use
export const useCartContext = () => {
  return useContext(CartContext);
};
