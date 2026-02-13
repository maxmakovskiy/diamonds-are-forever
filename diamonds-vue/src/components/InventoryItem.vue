<script setup>
import { defineProps } from 'vue';

defineProps(['item'])

</script>

<template>
  <div class="item">
        <div>Id: {{ item.lotId }}</div>
        <div>Stock name: {{ item.stockName }}</div>
        <div>Origin: {{ item.origin }}</div>
        <div>Purchase date: {{ item.purchaseDate }}</div>
        <div>Type: {{ item.type }}</div>
        <div>Updated: {{ item.updatedAt }}</div>
        <div>Created: {{ item.createdAt }}</div>
  </div>
</template>

<style scoped>
.item {
  margin-top: 2rem;
  display: flex;
  flex-direction: column;
  position: relative;
  border: 1pt solid black;
}


</style>
