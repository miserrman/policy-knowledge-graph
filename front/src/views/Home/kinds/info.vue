<template>
    <recommend v-bind:kind="kind"></recommend>
</template>

<script>
import Recommend from "@/views/Home/recommend";
export default {
    name:'info',
  components: {Recommend},
  data(){
      return{
        kind: '2'
      }
  }
}
</script>

<style scoped>

</style>
