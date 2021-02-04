<template>
  <div>
    <i class="el-icon-coin"></i>
    <el-input-number v-model="value"  size="mini" controls-position="right" :max="maxValue"></el-input-number>
    <el-button size="mini" @click="giveScore" >确定</el-button>
  </div>
</template>

<script>
    import axios from 'axios'
    export default {
        name: "answerScore",
        props: ['questionID', 'answerID', 'maxValue'],
        data(){
          return{
            value: 0
          }
        },
        mounted(){

        },
        methods: {
          giveScore: function () {
            this.$emit('distriScore', this.value)
            axios.get(`api/query/givescore?answerID=${this.answerID}&score=${this.value}`)
              .then(function(response){
              }).catch(function(err){})
          }
        }
    }
</script>

<style scoped>

</style>
