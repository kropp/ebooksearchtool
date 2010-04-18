import codecs

from django.core.management.base import BaseCommand

from queryspell.models import Dictionary, Words
from queryspell.trigram_tool import generate_trigram

class Command(BaseCommand):
    option_list = BaseCommand.option_list
    help = 'Load data from file to dictionary.'
    args = 'dictname filename'

    usage_str = 'Usage: manage.py loaddict [options] dictname filename'

    requires_model_validation = False


    def handle(self, dictname=None, filename=None, **options):
        if not dictname:
            print 'You must name dictionary for loading data'
            print self.usage_str
            return

        if not filename:
            print 'Please provide a file with data for reading'
            print self.usage_str
            return

        error_message = self.__load_data(dictname, filename)
        if error_message:
            print error_message
            


    def __load_data(self, dictname, filename):
        dictionary, is_created = Dictionary.objects.get_or_create(name=dictname)
        if not is_created:
            print 'Removing old data from dictionary ...'
            Words.objects.filter(dictionary=dictionary).delete()

        line_count = 0
        line = ''
        added_word_count = 0
        try:
            in_file = codecs.open(filename, mode='r', encoding='utf-8')
            
            print 'Loading new data ...'

            for line in in_file:
                line_count += 1
                if line:
                    word, freq = line.split()
                    if not word.isdigit():
                        added_word_count += 1
                        word = word.lower()
                        trigrams = u' '.join(generate_trigram(word))
                        Words.objects.create(word=word, trigrams=trigrams, \
                                             length=len(word), frequency=freq, \
                                             dictionary=dictionary)

            in_file.close()

        except ValueError, ex:
            return "Error in %s line: bad file format (%s)\n`%s'" % \
                   (line_count, ex, line)

        print 'Complete: %s words loaded into dict' % added_word_count
        

    def __generate_sphinx_conf(self):
        pass
