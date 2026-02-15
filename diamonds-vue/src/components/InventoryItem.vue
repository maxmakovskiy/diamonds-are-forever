<script setup>
import { defineProps, computed } from 'vue';

const props = defineProps(['item'])


function capitalize(val) {
  return String(val).charAt(0).toUpperCase() + String(val).slice(1);
}

const title = computed(() => capitalize(props.item.type))


</script>

<template>
  <div class="container"
  >
    <h3>{{ title }}</h3>
    <h4>{{ `Stock name: ${ item.stockName }` }}</h4>

    <div class="details">
        <div>Origin: {{ item.origin }}</div>
        <div>Origin: {{ item.origin }}</div>
        <div>Purchase date: {{ item.purchaseDate }}</div>

    </div>

  </div>
</template>

