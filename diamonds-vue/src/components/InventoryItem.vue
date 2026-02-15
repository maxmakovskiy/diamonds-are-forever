<script setup>
import { defineProps, computed } from 'vue';

const props = defineProps(['item'])


function capitalize(val) {
  return String(val).charAt(0).toUpperCase() + String(val).slice(1);
}

const title = computed(() => capitalize(props.item.type))


</script>

<template>
  <v-card
    :subtitle="`Stock name: ${ item.stockName }`"
    :title="title"
  >
    <v-card-text class="bg-surface-light pt-14">
        <div>Origin: {{ item.origin }}</div>
        <div>Origin: {{ item.origin }}</div>
        <div>Purchase date: {{ item.purchaseDate }}</div>

    </v-card-text>

  </v-card>
</template>

