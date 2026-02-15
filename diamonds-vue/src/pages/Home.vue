<script setup>
import { ref, onMounted } from 'vue'
import { default as axios } from 'axios'

import { Alert, InventoryItem } from '@/components'

const items = ref([]);

onMounted(async () => {
  await axios
    .get('http://localhost:8080/items')
    // .get('https://api.diamonds.ddnsfree.com/items')
    .then(response => {
        for (const item of response.data) {
            items.value.push(item)
        }
    })
});


</script>

<template>
  <v-main>
    <Alert />
    <v-container >
      <v-row >
        <v-col 
          v-for="item in items"
          cols="8"
        >
          <InventoryItem
              :key="item.lotId"
              :item="item"
          />
        </v-col>

      </v-row>
    </v-container>
  </v-main>
</template>
