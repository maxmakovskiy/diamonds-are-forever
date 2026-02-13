<script setup>
import { ref, onMounted } from 'vue'
import { default as axios } from 'axios'

import InventoryItem from './components/InventoryItem.vue'

const items = ref([]);

onMounted(async () => {
  await axios
    // .get('http://localhost:8080/items')
    .get('https://api.diamonds.ddnsfree.com/items')
    .then(response => {
        for (const item of response.data) {
            items.value.push(item)
        }
    })
});


</script>

<template>
    <InventoryItem
        v-for="item in items"
        :key="item.lotId"
        :item="item"
    />
</template>

<style scoped>

</style>
