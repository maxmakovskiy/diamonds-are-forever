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
    <v-navigation-drawer
      expand-on-hover
      permanent
      rail
    >
      <v-list>
        <v-list-item prepend-icon="mdi-handshake-outline">Details</v-list-item>
      </v-list>

      <v-divider></v-divider>

      <v-list density="compact" nav>
        <v-list-item prepend-icon="mdi-diamond" title="Items" value="items"></v-list-item>
        <v-list-item prepend-icon="mdi-login" title="Sign-in" value="login"></v-list-item>
        <v-list-item prepend-icon="mdi-information-outline" title="About" value="about"></v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar
      color="teal-darken-4"
    >
      <v-app-bar-title>Diamonds, Gemstones and Jewelries</v-app-bar-title>
    </v-app-bar>

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
