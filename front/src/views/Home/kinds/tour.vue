<template>
    <recommend v-bind:kind="kind"></recommend>
</template>

<script>
import Recommend from "@/views/Home/recommend";
export default {
    name:'tour',
  components: {Recommend},
  data(){
      return{
        kind: '5'
      }
  }
}
</script>

<style scoped>

</style>
