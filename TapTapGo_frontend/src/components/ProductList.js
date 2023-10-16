import React from 'react';
import { useProductsContext } from '../context/products_context';
import ListView from './ListView';

const ProductList = () => {
  const { products } = useProductsContext();
  return <ListView products={products}>product list</ListView>;
};

export default ProductList;
