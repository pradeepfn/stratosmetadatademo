# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
# 
#   http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.

class mysql{

  if $stratos_mysql_password {
    $root_password = $stratos_mysql_password
  }
  else {
    $root_password = 'root'
  }

  package { ['mysql-server','phpmyadmin','apache2']:
    ensure => installed,
	require  => Exec['apt-get update'],
  }
  
  exec { "apt-get update":
      command => "/usr/bin/apt-get update",
     # onlyif => "/bin/sh -c '[ ! -f /var/cache/apt/pkgcache.bin ] || /usr/bin/find /etc/apt/* -cnewer /var/cache/apt/pkgcache.bin | /bin/grep . > /dev/null'",
  }

  service { 'mysql':
    ensure  => running,
    pattern => 'mysql',
    require => Package['mysql-server'],
  }

  service { 'apache2':
    ensure  => running,
    pattern => 'apache2',
    require => Package['apache2'],
  }

  file { '/etc/apache2/conf.d/phpmyadmin.conf':
    ensure  => present,
    content => template('mysql/phpMyAdmin.conf.erb'),
    notify  => Service['apache2'],
    require => [
      Package['phpmyadmin'],
      Package['apache2'],
    ];
  }

  file { '/etc/mysql/my.cnf':
    ensure  => present,
    content => template('mysql/my.cnf.erb'),
    notify  => Service['apache2'],
    require => [
      Package['phpmyadmin'],
      Package['apache2'],
    ];
  }

  file { '/etc/apache2/sites-enabled/000-default':
    content => template('mysql/000-default.erb'),
    notify  => Service['apache2'],
    require => [
      Package['phpmyadmin'],
      Package['apache2'],
    ];
  }

  exec { 'Restart MySQL' :
    path    => "/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin",
    command => "/etc/init.d/mysql restart",
    require => File['/etc/apache2/sites-enabled/000-default'];
  }
  
  file { '/mnt/post.py':
    ensure  => present,
    content => template('mysql/post.py.erb'),
  }

  file { '/mnt/export_params.sh':
    ensure  => present,
    content => template('mysql/export_params.sh.erb'),
	mode    => '0755',
    require => [
	  File['/mnt/post.py'],
    ];
  }
  
  exec { 'Update metadata service' :
     user       => 'root',
     group      => 'root',
     path    => "/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin",
	 command => "/bin/bash /mnt/export_params.sh",
	 require => [
	               File['/mnt/export_params.sh'],
				   File['/mnt/post.py']   ];
  }
}
