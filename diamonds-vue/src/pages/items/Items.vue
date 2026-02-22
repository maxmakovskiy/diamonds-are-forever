<script setup>
import { ref, onMounted } from 'vue'
import { default as axios } from 'axios'
import { useAuthStore } from '@/stores'
import { router } from '@/router'
import InventoryItem from '@/components/InventoryItem.vue';


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

const authStore = useAuthStore();
if (authStore.user) {
    router.push('/items');
}

</script>

<template>
  <div id="items">
    <InventoryItem v-for="item in items" :item="item" /> 
  </div>
</template>


<style scoped>
#items {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  column-gap: 1em;
  row-gap: 1em;
}

</style>