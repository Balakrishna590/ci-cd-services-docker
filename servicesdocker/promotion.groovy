node('docker-maven-slave'){
	stage 'Build External Properties'
	//build 'RxLinkAppQA
	sh '''env=stage
	artifactory=http://repo1.uhc.com/artifactory
	repository=UHG-Snapshots
	project=com/optumrx/rxlinkapp
	repo_url=$artifactory/$repository/$project
	
	version='curl -s $repo_url/maven-metadata.xml|grep "<latest>"|sed "s/.*<latest>\\([^-]*\\)-SNAPSHOT<\\/latest>.*/\\1/"'
	build_no='curl -s $repo_url/$version-SNAPSHOT/maven-metadata.xml|grep "buildNumber"|sed "s/.*<buildNumber>\\([^<]*\\)<\\/buildNumber>.*/\\1/"'
	timestamp='curl -s $repo_url/$version-SNAPSHOT/maven-metadata.xml|grep "timestamp"|sed "s/.*<timestamp>\\([^<]*\\)<\\/timestamp>.*/\\1/"'
	
	latestWar=$repo_url/$version-SNAPSHOT/rxlinkapp-$version-$timestamp-$build_no.jar
	
	echo $version > version.properties
	ech $timestamp > timestamp.properties
	echo $build_no > build_no.properties
	stash name: "version-stash", include:"*"
	//notify('Sucess need approval for deploy')
}

//input 'Deploy to STAGE Environment?'

node(docker-maven-slave){
	withEnv(['USR_EMIAL'='balakrishna1024@gmail.com', 'OSE_USER':'bala590', 'OSE_PROJECT':'rxlink-stage', 'OSE_APP'='pcmsapp-' )