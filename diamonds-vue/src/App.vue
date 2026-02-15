<script setup>
</script>

<template>
<v-layout>
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
    
    <router-view />

</v-layout>


</template>

