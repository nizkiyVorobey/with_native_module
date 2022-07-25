import React, { useCallback, useEffect, useState } from 'react'
import {
  SafeAreaView,
  StyleSheet,
  Text,
  NativeModules,
  Button,
  TextInput,
  View,
} from 'react-native'

type ScheduleAlarmProps = {
  date: {
    hours: number
    minutes: number
    seconds: number
  }
  title: string
  description: string
}

interface AlarmModuleI {
  makeAlarm: (delay: number, title: string, description: string) => void
  makeScheduleAlarm: (props: ScheduleAlarmProps) => void
}

interface FlashlightModuleI {
  toggleFlashlight: (status: boolean, errorCallBack: (err: any) => void) => void
}
interface OpenMyCustomActivityI {
  open: () => void
}

interface NativeModulesI {
  AlarmModule: AlarmModuleI
  FlashlightModule: FlashlightModuleI
  OpenMyCustomActivity: OpenMyCustomActivityI
}

const { AlarmModule, FlashlightModule, OpenMyCustomActivity } =
  NativeModules as NativeModulesI

const getDate = () => {
  const time = new Date()
  return {
    hours: time.getHours().toString(),
    minutes: time.getMinutes().toString(),
    seconds: time.getSeconds().toString(),
  }
}

const App = () => {
  const [hours, setHours] = useState(getDate().hours)
  const [minutes, setMinutes] = useState(getDate().minutes)
  const [seconds, setSeconds] = useState(getDate().seconds)

  const [flashLightStatus, setFlashLightStatus] = useState(false)

  const handleMakeAlarm = () => {
    AlarmModule.makeAlarm(0, 'Слава Україні!', '')
  }

  const handleScheduleAlarm = () => {
    AlarmModule.makeScheduleAlarm({
      date: {
        hours: Number(hours),
        minutes: Number(minutes),
        seconds: Number(seconds),
      },
      title: 'Hola',
      description: 'Amigo!',
    })
  }

  const openSecondActivity = () => {
    OpenMyCustomActivity.open()
  }

  useEffect(() => {
    FlashlightModule.toggleFlashlight(flashLightStatus, (err: any) =>
      console.log('ERROR: ', err),
    )
  }, [flashLightStatus])

  const handleFlashlight = useCallback(() => {
    setFlashLightStatus(!flashLightStatus)
  }, [flashLightStatus])

  const flashlightTitle = flashLightStatus
    ? 'Turn off flashlight'
    : 'Turn on flashlight'

  return (
    <SafeAreaView>
      <View style={styles.inputContainer}>
        <Text style={styles.inputLabel}>Hours:</Text>
        <TextInput
          style={styles.input}
          placeholder="hours"
          defaultValue={hours}
          onChangeText={setHours}
          keyboardType="numeric"
        />
      </View>
      <View style={styles.inputContainer}>
        <Text style={styles.inputLabel}>Minutes:</Text>
        <TextInput
          style={styles.input}
          placeholder="minutes"
          defaultValue={minutes}
          onChangeText={setMinutes}
          keyboardType="numeric"
        />
      </View>
      <View style={styles.inputContainer}>
        <Text style={styles.inputLabel}>Seconds:</Text>
        <TextInput
          style={styles.input}
          placeholder="seconds"
          defaultValue={seconds}
          onChangeText={setSeconds}
          keyboardType="numeric"
        />
      </View>

      <View style={{ marginTop: 50 }}>
        <Button
          title="Schedule alarm"
          color="#841584"
          onPress={handleScheduleAlarm}
        />
      </View>

      <View style={{ marginTop: 50 }}>
        <Button
          title="Make immediate alarm!"
          color="orange"
          onPress={handleMakeAlarm}
        />
      </View>

      <View style={{ marginTop: 50 }}>
        <Button title={flashlightTitle} onPress={handleFlashlight} />
      </View>

      <View style={{ marginTop: 50 }}>
        <Button title={'Open second activity'} onPress={openSecondActivity} />
      </View>
    </SafeAreaView>
  )
}

const styles = StyleSheet.create({
  inputContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 20,
    marginBottom: 10,
  },
  inputLabel: {
    marginRight: 20,
    alignSelf: 'flex-end',
    fontWeight: 'bold',
    width: 70,
  },
  input: {
    width: 100,
    borderBottomColor: 'blue',
    borderBottomWidth: 2,
    paddingBottom: 0,
  },
})

export default App
