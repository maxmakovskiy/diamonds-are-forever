<script setup>
import { ref, onMounted } from 'vue';
import { default as axios } from 'axios';
// const axios = require("axios");

const items = ref([]);

onMounted(async () => {
  await axios
    .get(
        'https://diamonds.ddnsfree.com/items', { 
        headers: {
            "Access-Control-Allow-Origin": "*",
        }, 
    })
    .then(response => {
        console.log(response)
    })
});


</script>

<template>
    <ol>
    <li v-for="item in items">
    {{ item }}
    </li>

    </ol>
</template>

<style scoped>

</style>
