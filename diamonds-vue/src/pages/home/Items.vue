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
  <div class="container">
    <InventoryItem v-for="item in items" :item="item" /> 
  </div>
</template>


<style scoped>
.container {
  border: 1px solid black;
}
</style>