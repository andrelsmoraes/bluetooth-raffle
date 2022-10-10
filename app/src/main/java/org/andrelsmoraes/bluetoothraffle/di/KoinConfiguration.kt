package org.andrelsmoraes.bluetoothraffle.di

import org.andrelsmoraes.bluetoothraffle.data.repository.BluetoothDeviceRepositoryImpl
import org.andrelsmoraes.bluetoothraffle.data.repository.DeviceRepositoryImpl
import org.andrelsmoraes.bluetoothraffle.data.repository.PreferencesRepositoryImpl
import org.andrelsmoraes.bluetoothraffle.data.repository.RaffledRepositoryImpl
import org.andrelsmoraes.bluetoothraffle.data.repository.mock.MockedBluetoothDeviceRepositoryImpl
import org.andrelsmoraes.bluetoothraffle.data.storage.AllDevicesStorageImpl
import org.andrelsmoraes.bluetoothraffle.data.storage.RaffledDevicesStorageImpl
import org.andrelsmoraes.bluetoothraffle.domain.repository.BluetoothDeviceRepository
import org.andrelsmoraes.bluetoothraffle.domain.repository.DeviceRepository
import org.andrelsmoraes.bluetoothraffle.domain.repository.PreferencesRepository
import org.andrelsmoraes.bluetoothraffle.domain.repository.RaffledRepository
import org.andrelsmoraes.bluetoothraffle.domain.storage.AllDevicesStorage
import org.andrelsmoraes.bluetoothraffle.domain.storage.RaffledDevicesStorage
import org.andrelsmoraes.bluetoothraffle.domain.usecase.GetDeviceSearchPeriodUseCase
import org.andrelsmoraes.bluetoothraffle.domain.usecase.GetRaffledDevicesUseCase
import org.andrelsmoraes.bluetoothraffle.domain.usecase.GetRemainingDevicesUseCase
import org.andrelsmoraes.bluetoothraffle.domain.usecase.IsShakeEnabledUseCase
import org.andrelsmoraes.bluetoothraffle.domain.usecase.ObserveDevicesSizeUseCase
import org.andrelsmoraes.bluetoothraffle.domain.usecase.RaffleDeviceUseCase
import org.andrelsmoraes.bluetoothraffle.domain.usecase.StartDeviceSearchUseCase
import org.andrelsmoraes.bluetoothraffle.domain.usecase.StopDeviceSearchUseCase
import org.andrelsmoraes.bluetoothraffle.ui.DevicesViewModel
import org.andrelsmoraes.bluetoothraffle.ui.MainViewModel
import org.andrelsmoraes.bluetoothraffle.ui.RaffledViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<AllDevicesStorage> { AllDevicesStorageImpl() }
    single<RaffledDevicesStorage> { RaffledDevicesStorageImpl() }

    single<DeviceRepository> { DeviceRepositoryImpl(get()) }
    single<RaffledRepository> { RaffledRepositoryImpl(get()) }
    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }
    single<BluetoothDeviceRepository> { MockedBluetoothDeviceRepositoryImpl(get(), get()) }
//    single<BluetoothDeviceRepository> { BluetoothDeviceRepositoryImpl(get(), get()) }

    factory { GetDeviceSearchPeriodUseCase(get()) }
    factory { GetRaffledDevicesUseCase(get()) }
    factory { GetRemainingDevicesUseCase(get(), get()) }
    factory { IsShakeEnabledUseCase(get()) }
    factory { ObserveDevicesSizeUseCase(get(), get()) }
    factory { RaffleDeviceUseCase(get(), get(), get()) }
    factory { StartDeviceSearchUseCase(get(), get()) }
    factory { StopDeviceSearchUseCase(get()) }

    viewModel { MainViewModel(get()) }
    viewModel { DevicesViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { RaffledViewModel(get()) }

}
