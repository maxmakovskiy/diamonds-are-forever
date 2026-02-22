<script setup>
import { useRoute } from 'vue-router'

const route = useRoute();
const lotId = route.params.id;

</script>

<template>
  <div>items/Details for item with id {{ lotId }}</div>
</template>

