<script setup>
import { ref, onMounted } from 'vue'
import { default as axios } from 'axios'


const firstName = ref("")
const lastName = ref("")
const office = ref("")
const email = ref("")
const role = ref("")

onMounted(async () => {
  await axios
    .get('http://localhost:8080/profile', { withCredentials: true })
    .then(response => {
      firstName.value = response.data.firstName
      lastName.value = response.data.lastName
      office.value = response.data.counterpartName
      email.value = response.data.email
      role.value = response.data.role
    })
})

</script>

<template>
  <div id="profile-container">
    <h1>Profile</h1>
    <div id="profile">
        <p>First name: {{ firstName }}</p>
        <p>Last name: {{ lastName }}</p>
        <p>Email address: {{ email }}</p>
        <p>Office: {{ office }}</p>
        <p>Role at office: {{ role }}</p>
    </div>
    <div>
      <router-link id="change-password" to="/profile/change-password">
        Change Password
      </router-link>
    </div>
  </div>
</template>

<style scoped>
#profile-container {
  padding: 2em;

  display: flex;
  flex-direction: column;
}

#profile {
  margin-bottom: 2em;

  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  row-gap: 0.5em;
}

#profile > p {
  margin: 0;
  line-height: 1.385em;
}

#change-password {
  background: transparent;
  padding: 0.5em 1em;
  border: 3px solid black;
  border-radius: 5px;
  text-decoration: none;
  color: black;
  font-weight: bold;
}

#change-password:hover {
  background-color: #D8DDEF;
}

</style>
