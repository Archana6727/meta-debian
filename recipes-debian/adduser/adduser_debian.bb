#
# base recipe: http://cgit.openembedded.org/meta-openembedded/tree/meta-perl
# /recipes-perl/adduser/adduser_3.113+nmu3.bb
# base branch: master
#

PR = "r0"

inherit debian-package

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = " \
file://debian/copyright;md5=caed49ab166f22ef31bf1127f558d0ef"


inherit cpan-base update-alternatives

do_install() {
	install -d ${D}${sbindir}
	install -m 0755 ${S}/adduser ${D}${sbindir}
	install -m 0755 ${S}/deluser ${D}${sbindir}

	# base on debian/rules
	ln -s adduser ${D}${sbindir}/addgroup
	ln -s deluser ${D}${sbindir}/delgroup

	install -d ${D}${libdir}/perl/${PERLVERSION}/Debian
	install -m 0644 ${S}/AdduserCommon.pm \
		${D}${libdir}/perl/${PERLVERSION}/Debian
	sed -i -e "s/VERSION/${PV}/" ${D}${sbindir}/*

	install -d ${D}/${sysconfdir}
	install -m 0644 ${S}/*.conf ${D}/${sysconfdir}

	install -d ${D}${mandir}/man5
	install -m 0644 ${S}/doc/*.conf.5 ${D}${mandir}/man5
	install -d ${D}${mandir}/man8
	install -m 0644 ${S}/doc/*.8 ${D}${mandir}/man8
	install -d ${D}${docdir}/${BPN}
	cp -rf ${S}/examples ${D}${docdir}/${BPN}
}

RDEPENDS_${PN} += "\
	shadow \
	perl-module-getopt-long \
	perl-module-overloading \
	perl-module-file-find \
	perl-module-file-temp \
"

ALTERNATIVE_${PN} = "adduser deluser addgroup delgroup"
ALTERNATIVE_PRIORITY = "60"
ALTERNATIVE_LINK_NAME[adduser] = "${sbindir}/adduser"
ALTERNATIVE_LINK_NAME[deluser] = "${sbindir}/deluser"
ALTERNATIVE_LINK_NAME[addgroup] = "${sbindir}/addgroup"
ALTERNATIVE_LINK_NAME[delgroup] = "${sbindir}/delgroup"
ALTERNATIVE_TARGET[addgroup] = "${sbindir}/adduser.${BPN}"
ALTERNATIVE_TARGET[delgroup] = "${sbindir}/deluser.${BPN}"

DEBIAN_PATCH_TYPE = "nopatch"
