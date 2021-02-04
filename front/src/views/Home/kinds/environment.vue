<template>
    <recommend v-bind:kind="kind"></recommend>
</template>

<script>
import Recommend from "@/views/Home/recommend";
export default {
    name:'environment',
  components: {Recommend},
  data(){
      return{
        kind: '4'
      }
  }
}
</script>

<style scoped>

</style>
