import {
  ADD_TO_CART,
  COUNT_CART_TOTALS,
  REMOVE_CART_ITEM,
  TOGGLE_CART_ITEM_AMOUNT,
} from '../actions';

const cart_reducer = (state, action) => {
  // When dispatch action ADD_TO_CART from single product page (onclick of button), do the following
  if (action.type === ADD_TO_CART) {
    const { id, amount, product } = action.payload;
    // If a product is already in the cart, update the cart item by the amount toggle in Single product page
    const tempItem = state.cart.find((i) => i === id);
    if (tempItem) {
      const tempCart = state.cart.map((cartItem) => {
        // If the cart item is similar to the product toggled in single product page, update the quatity
        if (cartItem.id === id) {
          let newAmount = cartItem.amount + amount;
          // If the amount is greater than the item in stock, return the max stock
          if (newAmount > cartItem.max) {
            newAmount = cartItem.max;
          }
          // If the amount is still less than stock, then use the new amount
          return { ...cartItem, amount: newAmount };
          // If the cart item is not the product toggled in single product page, return it as it is.
        } else {
          return cartItem;
        }
      });
      return { ...state, cart: tempCart };
    } else {
      // If the product is not already in the cart, create a new cart array and add the product in the cart
      const newItem = {
        id: id,
        name: product.name,
        amount,
        image: product.images[0].url, // Select the first image
        price: product.price,
        max: product.stock,
      };
      return { ...state, cart: [...state.cart, newItem] };
    }
  }
  // When dispatch action REMOVE_CART_ITEM (onClick of FaTrash icon), remove the product from cart page
  if (action.type === REMOVE_CART_ITEM) {
    const tempCart = state.cart.filter((item) => item.id !== action.payload);
    return { ...state, cart: tempCart };
  }
  // When dispatch action TOGGLE_CART_ITEM_AMOUNT from Cart page, update the quantity of product im cart
  if (action.type === TOGGLE_CART_ITEM_AMOUNT) {
    const { id, value } = action.payload;
    const tempCart = state.cart.map((item) => {
      // Select the cart item being toggled
      if (item.id === id) {
        // If FaPlus icon is toggled on (value = inc, passed in from CartItem increase()), then update the product quantity in cart
        if (value === 'inc') {
          let newAmount = item.amount + 1;
          // If the amount is greater than stock, return max stock
          if (newAmount > item.max) {
            newAmount = item.max;
          }
          // If the amount is still less than stock, return the amount
          return { ...item, amount: newAmount };
        }
        // If FaMinus icon is toggled on (value = dec, passed in from CartItem decrease()), then update the product quantity in cart
        if (value === 'dec') {
          let newAmount = item.amount - 1;
          // If amount is less than 1, return amount = 1
          if (newAmount < 1) {
            newAmount = 1;
          }
          // If the amount is greater than 1, return the amount
          return { ...item, amount: newAmount };
        }
        // If item is not toggled on, return it as it is
      } else {
        return item;
      }
    });
    return { ...state, cart: tempCart };
  }
  // Whenever there are changes in the cart, dispatch COUNT_CART_TOTAL to update the amount in cart icon (in the navbar and sidebar)
  if (action.type === COUNT_CART_TOTALS) {
    // Reduce Syntax
    const { total_items, total_amount } = state.cart.reduce(
      (total, cartItem) => {
        const { amount, price } = cartItem;
        total.total_items += amount;
        total.total_amount += amount * price;
        return total;
      },
      {
        total_items: 0,
        total_amount: 0,
      }
    );
    return { ...state, total_items, total_amount };
  }
  throw new Error(`No Matching "${action.type}" - action type`);
};

export default cart_reducer;
